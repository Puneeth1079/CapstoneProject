package com.gitopscontrolcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {

    private Long id;
    private String name;
    private String namespace;
    private String gitRepo;
    private String targetRevision;
    private String syncStatus;
    private String healthStatus;
    private Boolean autoSync;
    private Boolean selfHeal;
    private ZonedDateTime updatedAt;
    private List<Map<String, Object>> pods;
}
