package com.gitopscontrolcenter.configuration;

import com.gitopscontrolcenter.entity.*;
import com.gitopscontrolcenter.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ApplicationRepository applicationRepository;
    private final RolloutRecordRepository rolloutRecordRepository;
    private final DoraMetricSnapshotRepository doraMetricSnapshotRepository;
    private final RollbackEventRepository rollbackEventRepository;
    private final ActivityEventRepository activityEventRepository;
    private final DeploymentRepository deploymentRepository;
    private final PipelineRunRepository pipelineRunRepository;

    @Override
    public void run(String... args) {
        if (applicationRepository.count() == 0) {
            log.info("Seeding initial GitOps Control Center data...");

            applicationRepository.saveAll(List.of(
                    ApplicationEntity.builder()
                            .name("cloudcart-backend")
                            .namespace("default")
                            .gitRepo("https://github.com/capstone-org/capstone-gitops-platform")
                            .targetRevision("main")
                            .syncStatus("Synced")
                            .healthStatus("Healthy")
                            .autoSync(true)
                            .selfHeal(true)
                            .updatedAt(ZonedDateTime.now())
                            .build(),
                    ApplicationEntity.builder()
                            .name("cloudcart-frontend")
                            .namespace("default")
                            .gitRepo("https://github.com/capstone-org/capstone-gitops-platform")
                            .targetRevision("main")
                            .syncStatus("Synced")
                            .healthStatus("Healthy")
                            .autoSync(true)
                            .selfHeal(true)
                            .updatedAt(ZonedDateTime.now())
                            .build()
            ));

            rolloutRecordRepository.save(
                    RolloutRecord.builder()
                            .applicationName("cloudcart-backend")
                            .namespace("default")
                            .stableVersion("cloudcart-backend:v1.2.0")
                            .canaryVersion("cloudcart-backend:a82fc31")
                            .currentWeight(10)
                            .currentStep(1)
                            .phase("Progressing")
                            .analysisStatus("Evaluating")
                            .startedAt(ZonedDateTime.now().minusMinutes(15))
                            .updatedAt(ZonedDateTime.now())
                            .build()
            );

            doraMetricSnapshotRepository.saveAll(List.of(
                    DoraMetricSnapshot.builder()
                            .periodType("daily")
                            .deploymentFrequency(5.0)
                            .leadTimeHours(1.2)
                            .changeFailureRatePct(0.0)
                            .mttrMinutes(5.0)
                            .snapshotDate(ZonedDateTime.now())
                            .build(),
                    DoraMetricSnapshot.builder()
                            .periodType("weekly")
                            .deploymentFrequency(3.8)
                            .leadTimeHours(1.8)
                            .changeFailureRatePct(4.2)
                            .mttrMinutes(12.4)
                            .snapshotDate(ZonedDateTime.now().minusDays(7))
                            .build(),
                    DoraMetricSnapshot.builder()
                            .periodType("monthly")
                            .deploymentFrequency(4.1)
                            .leadTimeHours(1.5)
                            .changeFailureRatePct(2.5)
                            .mttrMinutes(9.1)
                            .snapshotDate(ZonedDateTime.now().minusDays(30))
                            .build()
            ));

            rollbackEventRepository.save(
                    RollbackEvent.builder()
                            .applicationName("cloudcart-backend")
                            .failedVersion("cloudcart-backend:v1.3.0-rc1")
                            .restoredVersion("cloudcart-backend:v1.2.0")
                            .failureReason("Prometheus AnalysisTemplate threshold breach: HTTP Error Rate > 2%")
                            .failedMetric("HTTP Error Rate")
                            .measuredValue("8.4%")
                            .thresholdValue("<= 2.0%")
                            .rolloutStage("Canary Step 2 (25%)")
                            .trafficExposure("25%")
                            .detectedAt(ZonedDateTime.now().minusHours(2))
                            .rollbackStartedAt(ZonedDateTime.now().minusHours(2).plusSeconds(5))
                            .recoveredAt(ZonedDateTime.now().minusHours(2).plusSeconds(43))
                            .recoveryDurationSeconds(38)
                            .automatic(true)
                            .status("COMPLETED")
                            .build()
            );

            activityEventRepository.saveAll(List.of(
                    ActivityEvent.builder()
                            .eventType("GITOPS_SYNC")
                            .title("Argo CD Synchronized")
                            .description("CloudCart backend synchronized to commit a82fc31")
                            .applicationName("cloudcart-backend")
                            .severity("INFO")
                            .timestamp(ZonedDateTime.now().minusMinutes(20))
                            .build(),
                    ActivityEvent.builder()
                            .eventType("CANARY_START")
                            .title("Canary Rollout Triggered")
                            .description("Promoted to 10% canary traffic")
                            .applicationName("cloudcart-backend")
                            .severity("INFO")
                            .timestamp(ZonedDateTime.now().minusMinutes(15))
                            .build(),
                    ActivityEvent.builder()
                            .eventType("METRIC_ANALYSIS")
                            .title("Prometheus Metrics Normal")
                            .description("HTTP Success Rate 99.8%, P95 Latency 120ms")
                            .applicationName("cloudcart-backend")
                            .severity("INFO")
                            .timestamp(ZonedDateTime.now().minusMinutes(10))
                            .build()
            ));

            deploymentRepository.saveAll(List.of(
                    DeploymentEntity.builder()
                            .applicationName("cloudcart-backend")
                            .version("v1.2.0")
                            .environment("production")
                            .status("SUCCESS")
                            .commitSha("a82fc31")
                            .commitMessage("feat: enhanced catalog response caching and fault tolerance")
                            .author("DevOps Team")
                            .startedAt(ZonedDateTime.now().minusHours(4))
                            .completedAt(ZonedDateTime.now().minusHours(4).plusMinutes(2))
                            .build(),
                    DeploymentEntity.builder()
                            .applicationName("cloudcart-frontend")
                            .version("v1.2.0")
                            .environment("production")
                            .status("SUCCESS")
                            .commitSha("e321bf4")
                            .commitMessage("feat: modern cloudcart storefront interface")
                            .author("Frontend Team")
                            .startedAt(ZonedDateTime.now().minusHours(6))
                            .completedAt(ZonedDateTime.now().minusHours(6).plusMinutes(1))
                            .build()
            ));

            pipelineRunRepository.saveAll(List.of(
                    PipelineRun.builder()
                            .workflowName("CI/CD Build & Validate")
                            .commitSha("a82fc31")
                            .branch("main")
                            .author("Puneeth")
                            .status("completed")
                            .conclusion("success")
                            .startedAt(ZonedDateTime.now().minusHours(4))
                            .durationSeconds(95)
                            .imageTag("cloudcart-backend:v1.2.0")
                            .build(),
                    PipelineRun.builder()
                            .workflowName("Canary Automated Verification")
                            .commitSha("a82fc31")
                            .branch("main")
                            .author("Argo-Rollouts")
                            .status("completed")
                            .conclusion("success")
                            .startedAt(ZonedDateTime.now().minusHours(3))
                            .durationSeconds(120)
                            .imageTag("cloudcart-backend:a82fc31")
                            .build()
            ));

            log.info("GitOps Control Center initial data seeded successfully.");
        }
    }
}
