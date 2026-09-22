package com.gitopscontrolcenter.service.implementation;

import com.gitopscontrolcenter.dto.ApplicationResponse;
import com.gitopscontrolcenter.entity.ApplicationEntity;
import com.gitopscontrolcenter.integration.kubernetes.KubernetesClient;
import com.gitopscontrolcenter.repository.ApplicationRepository;
import com.gitopscontrolcenter.service.interfaces.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final KubernetesClient kubernetesClient;

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse getApplicationByName(String name) {
        ApplicationEntity app = applicationRepository.findByName(name)
                .orElseGet(() -> ApplicationEntity.builder()
                        .name(name)
                        .namespace("default")
                        .gitRepo("https://github.com/capstone-org/capstone-gitops-platform")
                        .targetRevision("main")
                        .syncStatus("Synced")
                        .healthStatus("Healthy")
                        .autoSync(true)
                        .selfHeal(true)
                        .build());

        return mapToResponse(app);
    }

    private ApplicationResponse mapToResponse(ApplicationEntity app) {
        return ApplicationResponse.builder()
                .id(app.getId())
                .name(app.getName())
                .namespace(app.getNamespace())
                .gitRepo(app.getGitRepo())
                .targetRevision(app.getTargetRevision())
                .syncStatus(app.getSyncStatus())
                .healthStatus(app.getHealthStatus())
                .autoSync(app.getAutoSync())
                .selfHeal(app.getSelfHeal())
                .updatedAt(app.getUpdatedAt())
                .pods(kubernetesClient.getPods(app.getName()))
                .build();
    }
}
