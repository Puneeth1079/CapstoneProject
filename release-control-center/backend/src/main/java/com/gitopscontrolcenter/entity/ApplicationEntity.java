package com.gitopscontrolcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String namespace;

    @Column(name = "git_repo", nullable = false)
    private String gitRepo;

    @Column(name = "target_revision", nullable = false)
    private String targetRevision;

    @Column(name = "sync_status", nullable = false)
    private String syncStatus;

    @Column(name = "health_status", nullable = false)
    private String healthStatus;

    @Column(name = "auto_sync", nullable = false)
    private Boolean autoSync;

    @Column(name = "self_heal", nullable = false)
    private Boolean selfHeal;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        updatedAt = ZonedDateTime.now();
        if (namespace == null) namespace = "default";
        if (targetRevision == null) targetRevision = "main";
        if (syncStatus == null) syncStatus = "Synced";
        if (healthStatus == null) healthStatus = "Healthy";
        if (autoSync == null) autoSync = true;
        if (selfHeal == null) selfHeal = true;
    }
}
