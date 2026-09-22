package com.gitopscontrolcenter.service.implementation;

import com.gitopscontrolcenter.dto.GitOpsStatusResponse;
import com.gitopscontrolcenter.integration.argocd.ArgoCdClient;
import com.gitopscontrolcenter.service.interfaces.ArgoCdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArgoCdServiceImpl implements ArgoCdService {

    private final ArgoCdClient argoCdClient;

    @Override
    public List<GitOpsStatusResponse> getGitOpsApplications() {
        return argoCdClient.getAllApplications().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GitOpsStatusResponse getGitOpsApplication(String name) {
        Map<String, Object> data = argoCdClient.getApplicationStatus(name);
        return mapToResponse(data);
    }

    private GitOpsStatusResponse mapToResponse(Map<String, Object> data) {
        return GitOpsStatusResponse.builder()
                .applicationName((String) data.getOrDefault("name", "cloudcart-backend"))
                .gitRevision((String) data.getOrDefault("revision", "a82fc31"))
                .desiredRevision("main")
                .syncStatus((String) data.getOrDefault("syncStatus", "Synced"))
                .healthStatus((String) data.getOrDefault("healthStatus", "Healthy"))
                .repositoryUrl((String) data.getOrDefault("repoUrl", "https://github.com/capstone-org/capstone-gitops-platform"))
                .repositoryPath((String) data.getOrDefault("path", "kubernetes/overlays/production"))
                .namespace("default")
                .cluster("https://kubernetes.default.svc")
                .lastSync(ZonedDateTime.now())
                .autoSync((Boolean) data.getOrDefault("autoSync", true))
                .selfHeal((Boolean) data.getOrDefault("selfHeal", true))
                .prune(true)
                .build();
    }
}
