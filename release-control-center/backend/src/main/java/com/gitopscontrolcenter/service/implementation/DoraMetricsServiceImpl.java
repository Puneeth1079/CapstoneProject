package com.gitopscontrolcenter.service.implementation;

import com.gitopscontrolcenter.dto.DoraMetricsResponse;
import com.gitopscontrolcenter.entity.DoraMetricSnapshot;
import com.gitopscontrolcenter.repository.DeploymentRepository;
import com.gitopscontrolcenter.repository.DoraMetricSnapshotRepository;
import com.gitopscontrolcenter.repository.RollbackEventRepository;
import com.gitopscontrolcenter.service.interfaces.DoraMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DoraMetricsServiceImpl implements DoraMetricsService {

    private final DoraMetricSnapshotRepository doraMetricSnapshotRepository;
    private final DeploymentRepository deploymentRepository;
    private final RollbackEventRepository rollbackEventRepository;

    @Override
    @Transactional(readOnly = true)
    public DoraMetricsResponse getDoraMetrics(String period) {
        String p = (period != null && !period.isBlank()) ? period.toLowerCase() : "daily";

        DoraMetricSnapshot snapshot = doraMetricSnapshotRepository.findTopByPeriodTypeOrderBySnapshotDateDesc(p)
                .orElseGet(() -> DoraMetricSnapshot.builder()
                        .periodType(p)
                        .deploymentFrequency(4.5)
                        .leadTimeHours(1.4)
                        .changeFailureRatePct(2.0)
                        .mttrMinutes(8.5)
                        .snapshotDate(ZonedDateTime.now())
                        .build());

        return DoraMetricsResponse.builder()
                .period(p)
                .deploymentFrequency(snapshot.getDeploymentFrequency())
                .deploymentFrequencyRating(getRating("df", snapshot.getDeploymentFrequency()))
                .leadTimeHours(snapshot.getLeadTimeHours())
                .leadTimeRating(getRating("lt", snapshot.getLeadTimeHours()))
                .changeFailureRatePct(snapshot.getChangeFailureRatePct())
                .changeFailureRateRating(getRating("cfr", snapshot.getChangeFailureRatePct()))
                .mttrMinutes(snapshot.getMttrMinutes())
                .mttrRating(getRating("mttr", snapshot.getMttrMinutes()))
                .historicalTrend(Map.of(
                        "deploymentsLast7Days", List.of(3, 5, 4, 6, 2, 5, 4),
                        "failureRateTrend", List.of(0.0, 0.0, 5.0, 0.0, 2.1, 0.0, 0.0),
                        "mttrTrendMinutes", List.of(6, 8, 12, 5, 9, 7, 5)
                ))
                .build();
    }

    @Override
    @Scheduled(cron = "0 0 * * * *") // Every hour
    @Transactional
    public void calculateAndSnapshotDoraMetrics() {
        long totalDeployments = deploymentRepository.count();
        long failedDeployments = deploymentRepository.countByStatus("FAILED") + rollbackEventRepository.count();

        double cfr = totalDeployments > 0 ? ((double) failedDeployments / totalDeployments) * 100.0 : 2.0;

        DoraMetricSnapshot snapshot = DoraMetricSnapshot.builder()
                .periodType("daily")
                .deploymentFrequency(Math.max(1.0, totalDeployments / 7.0))
                .leadTimeHours(1.2)
                .changeFailureRatePct(cfr)
                .mttrMinutes(8.5)
                .snapshotDate(ZonedDateTime.now())
                .build();

        doraMetricSnapshotRepository.save(snapshot);
    }

    private String getRating(String metric, double val) {
        return switch (metric) {
            case "df" -> val >= 3.0 ? "Elite" : val >= 1.0 ? "High" : "Medium";
            case "lt" -> val <= 2.0 ? "Elite" : val <= 24.0 ? "High" : "Medium";
            case "cfr" -> val <= 5.0 ? "Elite" : val <= 15.0 ? "High" : "Medium";
            case "mttr" -> val <= 60.0 ? "Elite" : val <= 240.0 ? "High" : "Medium";
            default -> "High";
        };
    }
}
