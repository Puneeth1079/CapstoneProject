package com.gitopscontrolcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "pipeline_runs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PipelineRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workflow_name", nullable = false)
    private String workflowName;

    @Column(name = "commit_sha", nullable = false)
    private String commitSha;

    @Column(nullable = false)
    private String branch;

    private String author;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String conclusion;

    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    @Column(name = "duration_seconds", nullable = false)
    private Integer durationSeconds;

    @Column(name = "image_tag")
    private String imageTag;

    @PrePersist
    protected void onCreate() {
        if (startedAt == null) startedAt = ZonedDateTime.now();
        if (branch == null) branch = "main";
        if (status == null) status = "completed";
        if (conclusion == null) conclusion = "success";
        if (durationSeconds == null) durationSeconds = 120;
    }
}
