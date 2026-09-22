package com.gitopscontrolcenter.controller;

import com.gitopscontrolcenter.dto.PipelineResponse;
import com.gitopscontrolcenter.service.interfaces.PipelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pipelines")
@RequiredArgsConstructor
public class PipelineController {

    private final PipelineService pipelineService;

    @GetMapping
    public ResponseEntity<List<PipelineResponse>> getPipelineRuns() {
        return ResponseEntity.ok(pipelineService.getPipelineRuns());
    }
}
