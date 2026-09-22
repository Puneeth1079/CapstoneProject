package com.gitopscontrolcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "rollout_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolloutRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_name", nullable = false, unique = true)
    private String applicationName;

    @Column(nullable = false)
    private String namespace;

    @Column(name = "stable_version", nullable = false)
    private String stableVersion;

    @Column(name = "canary_version")
    private String canaryVersion;

    @Column(name = "current_weight", nullable = false)
    private Integer currentWeight;

    @Column(name = "current_step", nullable = false)
    private Integer currentStep;

    @Column(nullable = false)
    private String phase;

    @Column(name = "analysis_status", nullable = false)
    private String analysisStatus;

    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        updatedAt = ZonedDateTime.now();
        if (startedAt == null) startedAt = ZonedDateTime.now();
        if (namespace == null) namespace = "default";
        if (currentWeight == null) currentWeight = 100;
        if (currentStep == null) currentStep = 0;
        if (phase == null) phase = "Healthy";
        if (analysisStatus == null) analysisStatus = "Passed";
    }
}
