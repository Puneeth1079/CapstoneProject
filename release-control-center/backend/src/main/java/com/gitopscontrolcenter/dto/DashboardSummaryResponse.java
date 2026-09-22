package com.gitopscontrolcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {

    private String heroMessage;
    private long totalApplications;
    private long healthyApplications;
    private long activeRollouts;
    private long failedDeployments;
    private String argoCdStatus;
    private String prometheusStatus;
    private Map<String, Object> clusterHealth;
    private RolloutResponse activeRolloutSummary;
    private DoraMetricsResponse doraSummary;
    private List<RollbackResponse> recentRollbacks;
    private List<ActivityEventResponse> recentActivity;
}
