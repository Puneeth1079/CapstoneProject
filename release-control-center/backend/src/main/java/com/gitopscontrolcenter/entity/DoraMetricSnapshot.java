package com.gitopscontrolcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "dora_metric_snapshots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoraMetricSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "period_type", nullable = false)
    private String periodType; // daily, weekly, monthly

    @Column(name = "deployment_frequency", nullable = false)
    private Double deploymentFrequency;

    @Column(name = "lead_time_hours", nullable = false)
    private Double leadTimeHours;

    @Column(name = "change_failure_rate_pct", nullable = false)
    private Double changeFailureRatePct;

    @Column(name = "mttr_minutes", nullable = false)
    private Double mttrMinutes;

    @Column(name = "snapshot_date")
    private ZonedDateTime snapshotDate;

    @PrePersist
    protected void onCreate() {
        if (snapshotDate == null) snapshotDate = ZonedDateTime.now();
        if (periodType == null) periodType = "daily";
    }
}
