package com.gitopscontrolcenter.controller;

import com.gitopscontrolcenter.dto.MetricResponse;
import com.gitopscontrolcenter.service.interfaces.PrometheusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class PrometheusController {

    private final PrometheusService prometheusService;

    @GetMapping("/{application}")
    public ResponseEntity<List<MetricResponse>> getApplicationMetrics(@PathVariable String application) {
        return ResponseEntity.ok(prometheusService.getApplicationMetrics(application));
    }
}
