package com.gitopscontrolcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "deployments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeploymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_name", nullable = false)
    private String applicationName;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private String environment;

    @Column(nullable = false)
    private String status;

    @Column(name = "commit_sha", nullable = false)
    private String commitSha;

    @Column(name = "commit_message", columnDefinition = "TEXT")
    private String commitMessage;

    private String author;

    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    @Column(name = "completed_at")
    private ZonedDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        if (startedAt == null) startedAt = ZonedDateTime.now();
        if (environment == null) environment = "production";
        if (status == null) status = "SUCCESS";
    }
}
