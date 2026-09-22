package com.gitopscontrolcenter.integration.github;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubClient {

    private final WebClient webClient;

    @Value("${devops.github.api-url:https://api.github.com}")
    private String githubApiUrl;

    @Value("${devops.github.repo:org/capstone-gitops-platform}")
    private String githubRepo;

    @Value("${devops.github.token:}")
    private String githubToken;

    public List<Map<String, Object>> getWorkflowRuns() {
        try {
            if (githubToken != null && !githubToken.isBlank()) {
                Map response = webClient.get()
                        .uri(githubApiUrl + "/repos/" + githubRepo + "/actions/runs")
                        .header("Authorization", "Bearer " + githubToken)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();
                if (response != null && response.containsKey("workflow_runs")) {
                    return (List<Map<String, Object>>) response.get("workflow_runs");
                }
            }
        } catch (Exception e) {
            log.debug("GitHub API query failed ({}), returning fallback pipeline runs", e.getMessage());
        }

        return List.of(
                Map.of(
                        "id", 101,
                        "workflowName", "CloudCart CI/CD Pipeline",
                        "commitSha", "a82fc31",
                        "branch", "main",
                        "author", "DevOps Engineer",
                        "status", "completed",
                        "conclusion", "success",
                        "startedAt", "2026-09-15T09:00:00Z",
                        "durationSeconds", 145,
                        "imageTag", "cloudcart-backend:a82fc31"
                ),
                Map.of(
                        "id", 100,
                        "workflowName", "CloudCart CI/CD Pipeline",
                        "commitSha", "f71e902",
                        "branch", "main",
                        "author", "Lead Architect",
                        "status", "completed",
                        "conclusion", "success",
                        "startedAt", "2026-09-14T14:30:00Z",
                        "durationSeconds", 132,
                        "imageTag", "cloudcart-backend:v1.2.0"
                )
        );
    }
}
