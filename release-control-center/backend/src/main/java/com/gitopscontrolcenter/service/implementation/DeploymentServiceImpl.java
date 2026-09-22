package com.gitopscontrolcenter.service.implementation;

import com.gitopscontrolcenter.dto.DeploymentResponse;
import com.gitopscontrolcenter.entity.DeploymentEntity;
import com.gitopscontrolcenter.repository.DeploymentRepository;
import com.gitopscontrolcenter.service.interfaces.DeploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeploymentServiceImpl implements DeploymentService {

    private final DeploymentRepository deploymentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DeploymentResponse> getAllDeployments() {
        return deploymentRepository.findTop10ByOrderByStartedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeploymentResponse> getDeploymentsByApplication(String appName) {
        return deploymentRepository.findByApplicationNameOrderByStartedAtDesc(appName).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DeploymentResponse getDeploymentById(Long id) {
        DeploymentEntity entity = deploymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deployment not found with id: " + id));
        return mapToResponse(entity);
    }

    private DeploymentResponse mapToResponse(DeploymentEntity entity) {
        return DeploymentResponse.builder()
                .id(entity.getId())
                .applicationName(entity.getApplicationName())
                .version(entity.getVersion())
                .environment(entity.getEnvironment())
                .status(entity.getStatus())
                .commitSha(entity.getCommitSha())
                .commitMessage(entity.getCommitMessage())
                .author(entity.getAuthor())
                .startedAt(entity.getStartedAt())
                .completedAt(entity.getCompletedAt())
                .build();
    }
}
