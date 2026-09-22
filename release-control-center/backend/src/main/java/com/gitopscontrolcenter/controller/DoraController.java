package com.gitopscontrolcenter.controller;

import com.gitopscontrolcenter.dto.DoraMetricsResponse;
import com.gitopscontrolcenter.service.interfaces.DoraMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dora")
@RequiredArgsConstructor
public class DoraController {

    private final DoraMetricsService doraMetricsService;

    @GetMapping
    public ResponseEntity<DoraMetricsResponse> getDoraMetrics(@RequestParam(defaultValue = "daily") String period) {
        return ResponseEntity.ok(doraMetricsService.getDoraMetrics(period));
    }
}
