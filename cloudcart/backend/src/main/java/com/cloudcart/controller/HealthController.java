package com.cloudcart.controller;

import com.cloudcart.service.interfaces.FaultSimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final FaultSimulationService faultSimulationService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getHealth() {
        faultSimulationService.evaluateAndApplyFault();
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "application", "cloudcart-backend",
                "timestamp", ZonedDateTime.now(),
                "faultMode", faultSimulationService.getFaultMode().getActiveMode()
        ));
    }
}
