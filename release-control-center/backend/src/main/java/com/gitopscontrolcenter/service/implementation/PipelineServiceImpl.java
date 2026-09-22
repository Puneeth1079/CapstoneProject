package com.gitopscontrolcenter.service.implementation;

import com.gitopscontrolcenter.dto.PipelineResponse;
import com.gitopscontrolcenter.integration.github.GitHubClient;
import com.gitopscontrolcenter.service.interfaces.PipelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PipelineServiceImpl implements PipelineService {

    private final GitHubClient gitHubClient;

    @Override
    public List<PipelineResponse> getPipelineRuns() {
        List<Map<String, Object>> runs = gitHubClient.getWorkflowRuns();
        return runs.stream().map(run -> PipelineResponse.builder()
                .id(Long.valueOf(run.getOrDefault("id", 101).toString()))
                .workflowName((String) run.getOrDefault("workflowName", "CloudCart CI/CD Pipeline"))
                .commitSha((String) run.getOrDefault("commitSha", "a82fc31"))
                .branch((String) run.getOrDefault("branch", "main"))
                .author((String) run.getOrDefault("author", "DevOps Engineer"))
                .status((String) run.getOrDefault("status", "completed"))
                .conclusion((String) run.getOrDefault("conclusion", "success"))
                .startedAt(ZonedDateTime.now().minusHours(2))
                .durationSeconds(Integer.valueOf(run.getOrDefault("durationSeconds", 145).toString()))
                .imageTag((String) run.getOrDefault("imageTag", "cloudcart-backend:a82fc31"))
                .build()
        ).collect(Collectors.toList());
    }
}
