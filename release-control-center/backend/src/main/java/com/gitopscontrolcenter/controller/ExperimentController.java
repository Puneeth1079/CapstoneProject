package com.gitopscontrolcenter.controller;

import com.gitopscontrolcenter.dto.ExperimentRequest;
import com.gitopscontrolcenter.dto.ExperimentResponse;
import com.gitopscontrolcenter.service.interfaces.ReleaseExperimentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/experiments")
@RequiredArgsConstructor
public class ExperimentController {

    private final ReleaseExperimentService experimentService;

    @PostMapping
    public ResponseEntity<ExperimentResponse> triggerExperiment(@Valid @RequestBody ExperimentRequest request) {
        return ResponseEntity.ok(experimentService.triggerExperiment(request));
    }

    @GetMapping("/active")
    public ResponseEntity<ExperimentResponse> getActiveExperiment() {
        return ResponseEntity.ok(experimentService.getActiveExperiment());
    }

    @PostMapping("/reset")
    public ResponseEntity<ExperimentResponse> resetExperiment() {
        return ResponseEntity.ok(experimentService.resetExperiment());
    }
}
