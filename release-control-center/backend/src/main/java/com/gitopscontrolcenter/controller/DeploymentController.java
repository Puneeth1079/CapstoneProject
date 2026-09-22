package com.gitopscontrolcenter.controller;

import com.gitopscontrolcenter.dto.DeploymentResponse;
import com.gitopscontrolcenter.service.interfaces.DeploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deployments")
@RequiredArgsConstructor
public class DeploymentController {

    private final DeploymentService deploymentService;

    @GetMapping
    public ResponseEntity<List<DeploymentResponse>> getAllDeployments() {
        return ResponseEntity.ok(deploymentService.getAllDeployments());
    }

    @GetMapping("/application/{appName}")
    public ResponseEntity<List<DeploymentResponse>> getDeploymentsByApplication(@PathVariable String appName) {
        return ResponseEntity.ok(deploymentService.getDeploymentsByApplication(appName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeploymentResponse> getDeploymentById(@PathVariable Long id) {
        return ResponseEntity.ok(deploymentService.getDeploymentById(id));
    }
}
