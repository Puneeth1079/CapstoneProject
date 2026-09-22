package com.gitopscontrolcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolloutResponse {

    private Long id;
    private String applicationName;
    private String namespace;
    private String stableVersion;
    private String canaryVersion;
    private Integer currentWeight;
    private Integer currentStep;
    private Integer totalSteps;
    private String phase;
    private String analysisStatus;
    private ZonedDateTime startedAt;
    private ZonedDateTime updatedAt;
    private List<Map<String, Object>> metricsAnalysis;
}
