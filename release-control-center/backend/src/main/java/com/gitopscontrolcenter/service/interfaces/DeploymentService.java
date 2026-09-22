package com.gitopscontrolcenter.service.interfaces;

import com.gitopscontrolcenter.dto.DeploymentResponse;

import java.util.List;

public interface DeploymentService {
    List<DeploymentResponse> getAllDeployments();
    List<DeploymentResponse> getDeploymentsByApplication(String appName);
    DeploymentResponse getDeploymentById(Long id);
}
