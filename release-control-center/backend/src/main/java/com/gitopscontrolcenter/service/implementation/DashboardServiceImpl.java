package com.gitopscontrolcenter.service.implementation;

import com.gitopscontrolcenter.dto.*;
import com.gitopscontrolcenter.repository.*;
import com.gitopscontrolcenter.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ApplicationRepository applicationRepository;
    private final DeploymentRepository deploymentRepository;
    private final RolloutRecordRepository rolloutRecordRepository;
    private final RolloutService rolloutService;
    private final DoraMetricsService doraMetricsService;
    private final RollbackService rollbackService;
    private final ActivityService activityService;
    private final KubernetesService kubernetesService;

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary() {
        long totalApps = applicationRepository.count();
        long healthyApps = applicationRepository.countByHealthStatus("Healthy");
        long activeRollouts = rolloutRecordRepository.countByPhase("Progressing");
        long failedDeployments = deploymentRepository.countByStatus("FAILED");

        RolloutResponse activeRollout = rolloutService.getRolloutByApplication("cloudcart-backend");
        DoraMetricsResponse doraSummary = doraMetricsService.getDoraMetrics("daily");
        List<RollbackResponse> recentRollbacks = rollbackService.getAllRollbacks();
        List<ActivityEventResponse> recentActivity = activityService.getRecentActivity();

        return DashboardSummaryResponse.builder()
                .heroMessage("Release confidently. Detect failures early. Roll back automatically.")
                .totalApplications(totalApps)
                .healthyApplications(healthyApps)
                .activeRollouts(activeRollouts)
                .failedDeployments(failedDeployments)
                .argoCdStatus("Connected (Synced)")
                .prometheusStatus("Connected (Scraping)")
                .clusterHealth(kubernetesService.getClusterStatus())
                .activeRolloutSummary(activeRollout)
                .doraSummary(doraSummary)
                .recentRollbacks(recentRollbacks)
                .recentActivity(recentActivity)
                .build();
    }
}
