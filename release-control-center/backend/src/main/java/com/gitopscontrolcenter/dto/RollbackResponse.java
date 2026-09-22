package com.gitopscontrolcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RollbackResponse {

    private Long id;
    private String applicationName;
    private String failedVersion;
    private String restoredVersion;
    private String failureReason;
    private String failedMetric;
    private String measuredValue;
    private String thresholdValue;
    private String rolloutStage;
    private String trafficExposure;
    private ZonedDateTime detectedAt;
    private ZonedDateTime rollbackStartedAt;
    private ZonedDateTime recoveredAt;
    private Integer recoveryDurationSeconds;
    private Boolean automatic;
    private String status;
}
