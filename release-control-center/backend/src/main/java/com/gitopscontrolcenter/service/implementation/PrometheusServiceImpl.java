package com.gitopscontrolcenter.service.implementation;

import com.gitopscontrolcenter.dto.MetricResponse;
import com.gitopscontrolcenter.integration.prometheus.PrometheusClient;
import com.gitopscontrolcenter.repository.ReleaseExperimentRepository;
import com.gitopscontrolcenter.service.interfaces.PrometheusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrometheusServiceImpl implements PrometheusService {

    private final PrometheusClient prometheusClient;
    private final ReleaseExperimentRepository experimentRepository;

    @Override
    public List<MetricResponse> getApplicationMetrics(String applicationName) {
        String activeFault = experimentRepository.findTopByActiveTrueOrderByStartedAtDesc()
                .map(exp -> exp.getMode())
                .orElse("NORMAL");

        Map<String, Object> liveMetrics = prometheusClient.getLiveApplicationMetrics(activeFault);

        double successRate = (double) liveMetrics.get("httpSuccessRate");
        double errorRate = (double) liveMetrics.get("httpErrorRate");
        double latency = (double) liveMetrics.get("p95LatencyMs");
        double txSuccess = (double) liveMetrics.get("transactionSuccessRate");
        double cpu = (double) liveMetrics.get("cpuUsagePct");
        double memory = (double) liveMetrics.get("memoryUsagePct");

        return List.of(
                MetricResponse.builder()
                        .name("HTTP Success Rate")
                        .currentValue(String.format("%.1f%%", successRate))
                        .threshold(">= 98.0%")
                        .status(successRate >= 98.0 ? "PASS" : "FAIL")
                        .trend(successRate >= 98.0 ? "STABLE" : "DEGRADING")
                        .lastChecked(ZonedDateTime.now())
                        .build(),
                MetricResponse.builder()
                        .name("HTTP Error Rate")
                        .currentValue(String.format("%.1f%%", errorRate))
                        .threshold("<= 2.0%")
                        .status(errorRate <= 2.0 ? "PASS" : "FAIL")
                        .trend(errorRate <= 2.0 ? "STABLE" : "DEGRADING")
                        .lastChecked(ZonedDateTime.now())
                        .build(),
                MetricResponse.builder()
                        .name("P95 Request Latency")
                        .currentValue(String.format("%.0fms", latency))
                        .threshold("<= 500ms")
                        .status(latency <= 500.0 ? "PASS" : "FAIL")
                        .trend(latency <= 500.0 ? "STABLE" : "DEGRADING")
                        .lastChecked(ZonedDateTime.now())
                        .build(),
                MetricResponse.builder()
                        .name("Transaction Success Rate")
                        .currentValue(String.format("%.1f%%", txSuccess))
                        .threshold(">= 97.0%")
                        .status(txSuccess >= 97.0 ? "PASS" : "FAIL")
                        .trend(txSuccess >= 97.0 ? "STABLE" : "DEGRADING")
                        .lastChecked(ZonedDateTime.now())
                        .build(),
                MetricResponse.builder()
                        .name("Pod CPU Utilization")
                        .currentValue(String.format("%.1f%%", cpu))
                        .threshold("<= 80.0%")
                        .status(cpu <= 80.0 ? "PASS" : "WARNING")
                        .trend(cpu <= 80.0 ? "STABLE" : "DEGRADING")
                        .lastChecked(ZonedDateTime.now())
                        .build(),
                MetricResponse.builder()
                        .name("Pod Memory Utilization")
                        .currentValue(String.format("%.1f%%", memory))
                        .threshold("<= 85.0%")
                        .status(memory <= 85.0 ? "PASS" : "WARNING")
                        .trend(memory <= 85.0 ? "STABLE" : "DEGRADING")
                        .lastChecked(ZonedDateTime.now())
                        .build()
        );
    }
}
