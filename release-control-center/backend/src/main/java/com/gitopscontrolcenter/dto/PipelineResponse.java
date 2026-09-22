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
public class PipelineResponse {

    private Long id;
    private String workflowName;
    private String commitSha;
    private String branch;
    private String author;
    private String status;
    private String conclusion;
    private ZonedDateTime startedAt;
    private Integer durationSeconds;
    private String imageTag;
}
