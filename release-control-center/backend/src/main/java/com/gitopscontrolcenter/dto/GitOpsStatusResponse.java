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
public class GitOpsStatusResponse {

    private String applicationName;
    private String gitRevision;
    private String desiredRevision;
    private String syncStatus;
    private String healthStatus;
    private String repositoryUrl;
    private String repositoryPath;
    private String namespace;
    private String cluster;
    private ZonedDateTime lastSync;
    private Boolean autoSync;
    private Boolean selfHeal;
    private Boolean prune;
}
