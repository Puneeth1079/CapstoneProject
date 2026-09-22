package com.gitopscontrolcenter.controller;

import com.gitopscontrolcenter.dto.GitOpsStatusResponse;
import com.gitopscontrolcenter.service.interfaces.ArgoCdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/argocd")
@RequiredArgsConstructor
public class ArgoCdController {

    private final ArgoCdService argoCdService;

    @GetMapping("/applications")
    public ResponseEntity<List<GitOpsStatusResponse>> getGitOpsApplications() {
        return ResponseEntity.ok(argoCdService.getGitOpsApplications());
    }

    @GetMapping("/applications/{name}")
    public ResponseEntity<GitOpsStatusResponse> getGitOpsApplication(@PathVariable String name) {
        return ResponseEntity.ok(argoCdService.getGitOpsApplication(name));
    }
}
