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
public class DeploymentResponse {

    private Long id;
    private String applicationName;
    private String version;
    private String environment;
    private String status;
    private String commitSha;
    private String commitMessage;
    private String author;
    private ZonedDateTime startedAt;
    private ZonedDateTime completedAt;
}
