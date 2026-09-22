package com.gitopscontrolcenter.service.implementation;

import com.gitopscontrolcenter.dto.RolloutResponse;

import com.gitopscontrolcenter.entity.RolloutRecord;
import com.gitopscontrolcenter.integration.kubernetes.KubernetesClient;
import com.gitopscontrolcenter.integration.prometheus.PrometheusClient;
import com.gitopscontrolcenter.repository.ReleaseExperimentRepository;
import com.gitopscontrolcenter.repository.RolloutRecordRepository;
import com.gitopscontrolcenter.service.interfaces.ActivityService;
import com.gitopscontrolcenter.service.interfaces.RollbackService;
import com.gitopscontrolcenter.service.interfaces.RolloutService;
import com.gitopscontrolcenter.dto.RollbackResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RolloutServiceImpl implements RolloutService {

    private final RolloutRecordRepository rolloutRecordRepository;
    private final KubernetesClient kubernetesClient;
    private final PrometheusClient prometheusClient;
    private final ReleaseExperimentRepository experimentRepository;
    private final RollbackService rollbackService;
    private final ActivityService activityService;

    @Override
    @Transactional(readOnly = true)
    public List<RolloutResponse> getAllRollouts() {
        return rolloutRecordRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RolloutResponse getRolloutByApplication(String applicationName) {
        RolloutRecord record = rolloutRecordRepository.findByApplicationName(applicationName)
                .orElseGet(() -> {
                    RolloutRecord newRecord = RolloutRecord.builder()
                            .applicationName(applicationName)
                            .namespace("default")
                            .stableVersion(applicationName + ":v1.2.0")
                            .canaryVersion(applicationName + ":a82fc31")
                            .currentWeight(10)
                            .currentStep(1)
                            .phase("Progressing")
                            .analysisStatus("Evaluating")
                            .build();
                    return rolloutRecordRepository.save(newRecord);
                });

        // Evaluate live fault mode impact on rollout analysis status
        String activeFault = experimentRepository.findTopByActiveTrueOrderByStartedAtDesc()
                .map(exp -> exp.getMode())
                .orElse("NORMAL");

        if (!"NORMAL".equalsIgnoreCase(activeFault) && "Progressing".equalsIgnoreCase(record.getPhase())) {
            record.setAnalysisStatus("Failed");
            record.setPhase("Degraded");
            rolloutRecordRepository.save(record);

            // Record automated rollback
            rollbackService.recordRollback(RollbackResponse.builder()
                    .applicationName(applicationName)
                    .failedVersion(record.getCanaryVersion())
                    .restoredVersion(record.getStableVersion())
                    .failureReason("Prometheus AnalysisTemplate threshold breach under " + activeFault + " experiment mode")
                    .failedMetric("HTTP Error / Latency Threshold")
                    .measuredValue("HIGH_LATENCY / HTTP_500")
                    .thresholdValue("Success Rate >= 98%, P95 <= 500ms")
                    .rolloutStage("Canary Step " + record.getCurrentStep() + " (" + record.getCurrentWeight() + "%)")
                    .trafficExposure(record.getCurrentWeight() + "%")
                    .recoveryDurationSeconds(35)
                    .automatic(true)
                    .status("COMPLETED")
                    .build());

            activityService.recordActivity("ROLLBACK_TRIGGERED",
                    "Automated Rollback Triggered for " + applicationName,
                    "AnalysisTemplate failed on Prometheus metrics. Traffic reverted to " + record.getStableVersion(),
                    applicationName, "CRITICAL");
        }

        return mapToResponse(record);
    }

    @Override
    @Transactional
    public RolloutResponse promoteRollout(String applicationName) {
        kubernetesClient.promoteRollout(applicationName);
        RolloutRecord record = getEntity(applicationName);

        int nextWeight = Math.min(100, record.getCurrentWeight() + 25);
        int nextStep = record.getCurrentStep() + 1;

        record.setCurrentWeight(nextWeight);
        record.setCurrentStep(nextStep);
        if (nextWeight == 100) {
            record.setPhase("Healthy");
            record.setStableVersion(record.getCanaryVersion());
            record.setCanaryVersion(null);
            record.setAnalysisStatus("Passed");
        } else {
            record.setPhase("Progressing");
            record.setAnalysisStatus("Evaluating");
        }

        RolloutRecord updated = rolloutRecordRepository.save(record);
        activityService.recordActivity("CANARY_PROMOTE",
                "Canary Rollout Promoted",
                "Traffic weight increased to " + nextWeight + "% for " + applicationName,
                applicationName, "INFO");
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public RolloutResponse pauseRollout(String applicationName) {
        kubernetesClient.pauseRollout(applicationName);
        RolloutRecord record = getEntity(applicationName);
        record.setPhase("Paused");
        RolloutRecord updated = rolloutRecordRepository.save(record);
        activityService.recordActivity("ROLLOUT_PAUSED", "Rollout Paused", "Rollout paused by user operator", applicationName, "WARNING");
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public RolloutResponse resumeRollout(String applicationName) {
        kubernetesClient.resumeRollout(applicationName);
        RolloutRecord record = getEntity(applicationName);
        record.setPhase("Progressing");
        RolloutRecord updated = rolloutRecordRepository.save(record);
        activityService.recordActivity("ROLLOUT_RESUMED", "Rollout Resumed", "Rollout resumed by user operator", applicationName, "INFO");
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public RolloutResponse abortRollout(String applicationName) {
        kubernetesClient.abortRollout(applicationName);
        RolloutRecord record = getEntity(applicationName);
        record.setPhase("Degraded");
        record.setAnalysisStatus("Failed");
        record.setCurrentWeight(0);

        RolloutRecord updated = rolloutRecordRepository.save(record);

        rollbackService.recordRollback(RollbackResponse.builder()
                .applicationName(applicationName)
                .failedVersion(record.getCanaryVersion() != null ? record.getCanaryVersion() : "v2-candidate")
                .restoredVersion(record.getStableVersion())
                .failureReason("Manual Abort command executed via GitOps Control Center")
                .failedMetric("Operator Override")
                .measuredValue("Abort Triggered")
                .thresholdValue("N/A")
                .rolloutStage("Canary Step " + record.getCurrentStep() + " (" + record.getCurrentWeight() + "%)")
                .trafficExposure("0%")
                .recoveryDurationSeconds(15)
                .automatic(false)
                .status("COMPLETED")
                .build());

        activityService.recordActivity("ROLLOUT_ABORTED", "Rollout Aborted & Rolled Back",
                "Candidate release aborted. Traffic restored to stable release " + record.getStableVersion(), applicationName, "HIGH");

        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public RolloutResponse retryRollout(String applicationName) {
        kubernetesClient.retryRollout(applicationName);
        RolloutRecord record = getEntity(applicationName);
        record.setPhase("Progressing");
        record.setAnalysisStatus("Evaluating");
        record.setCurrentWeight(10);
        record.setCurrentStep(1);
        RolloutRecord updated = rolloutRecordRepository.save(record);
        activityService.recordActivity("ROLLOUT_RETRY", "Rollout Retried", "Rollout reset to 10% canary stage", applicationName, "INFO");
        return mapToResponse(updated);
    }

    private RolloutRecord getEntity(String appName) {
        return rolloutRecordRepository.findByApplicationName(appName)
                .orElseGet(() -> RolloutRecord.builder()
                        .applicationName(appName)
                        .namespace("default")
                        .stableVersion(appName + ":v1.2.0")
                        .canaryVersion(appName + ":a82fc31")
                        .currentWeight(10)
                        .currentStep(1)
                        .phase("Progressing")
                        .analysisStatus("Evaluating")
                        .build());
    }

    private RolloutResponse mapToResponse(RolloutRecord record) {
        String activeFault = experimentRepository.findTopByActiveTrueOrderByStartedAtDesc()
                .map(exp -> exp.getMode())
                .orElse("NORMAL");

        Map<String, Object> metricsMap = prometheusClient.getLiveApplicationMetrics(activeFault);

        List<Map<String, Object>> metricsAnalysisList = new ArrayList<>();
        metricsAnalysisList.add(Map.of("name", "HTTP Success Rate", "value", metricsMap.get("httpSuccessRate") + "%", "threshold", ">= 98.0%", "status", (double)metricsMap.get("httpSuccessRate") >= 98.0 ? "PASS" : "FAIL"));
        metricsAnalysisList.add(Map.of("name", "P95 Latency", "value", metricsMap.get("p95LatencyMs") + "ms", "threshold", "<= 500ms", "status", (double)metricsMap.get("p95LatencyMs") <= 500.0 ? "PASS" : "FAIL"));
        metricsAnalysisList.add(Map.of("name", "Transaction Success Rate", "value", metricsMap.get("transactionSuccessRate") + "%", "threshold", ">= 97.0%", "status", (double)metricsMap.get("transactionSuccessRate") >= 97.0 ? "PASS" : "FAIL"));

        return RolloutResponse.builder()
                .id(record.getId())
                .applicationName(record.getApplicationName())
                .namespace(record.getNamespace())
                .stableVersion(record.getStableVersion())
                .canaryVersion(record.getCanaryVersion())
                .currentWeight(record.getCurrentWeight())
                .currentStep(record.getCurrentStep())
                .totalSteps(5)
                .phase(record.getPhase())
                .analysisStatus(record.getAnalysisStatus())
                .startedAt(record.getStartedAt())
                .updatedAt(record.getUpdatedAt())
                .metricsAnalysis(metricsAnalysisList)
                .build();
    }
}
