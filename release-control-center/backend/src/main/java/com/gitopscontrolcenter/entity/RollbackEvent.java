package com.gitopscontrolcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "rollback_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RollbackEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_name", nullable = false)
    private String applicationName;

    @Column(name = "failed_version", nullable = false)
    private String failedVersion;

    @Column(name = "restored_version", nullable = false)
    private String restoredVersion;

    @Column(name = "failure_reason", columnDefinition = "TEXT", nullable = false)
    private String failureReason;

    @Column(name = "failed_metric", nullable = false)
    private String failedMetric;

    @Column(name = "measured_value", nullable = false)
    private String measuredValue;

    @Column(name = "threshold_value", nullable = false)
    private String thresholdValue;

    @Column(name = "rollout_stage", nullable = false)
    private String rolloutStage;

    @Column(name = "traffic_exposure", nullable = false)
    private String trafficExposure;

    @Column(name = "detected_at")
    private ZonedDateTime detectedAt;

    @Column(name = "rollback_started_at")
    private ZonedDateTime rollbackStartedAt;

    @Column(name = "recovered_at")
    private ZonedDateTime recoveredAt;

    @Column(name = "recovery_duration_seconds", nullable = false)
    private Integer recoveryDurationSeconds;

    @Column(nullable = false)
    private Boolean automatic;

    @Column(nullable = false)
    private String status;

    @PrePersist
    protected void onCreate() {
        if (detectedAt == null) detectedAt = ZonedDateTime.now();
        if (rollbackStartedAt == null) rollbackStartedAt = ZonedDateTime.now();
        if (recoveredAt == null) recoveredAt = ZonedDateTime.now();
        if (recoveryDurationSeconds == null) recoveryDurationSeconds = 45;
        if (automatic == null) automatic = true;
        if (status == null) status = "COMPLETED";
    }
}
