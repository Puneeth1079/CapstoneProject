package com.cloudcart.controller;

import com.cloudcart.dto.FaultModeRequest;
import com.cloudcart.dto.FaultModeResponse;
import com.cloudcart.service.interfaces.FaultSimulationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo")
@RequiredArgsConstructor
public class DemoController {

    private final FaultSimulationService faultSimulationService;

    @PostMapping("/fault-mode")
    public ResponseEntity<FaultModeResponse> setFaultMode(@Valid @RequestBody FaultModeRequest request) {
        return ResponseEntity.ok(faultSimulationService.setFaultMode(request));
    }

    @GetMapping("/fault-mode")
    public ResponseEntity<FaultModeResponse> getFaultMode() {
        return ResponseEntity.ok(faultSimulationService.getFaultMode());
    }

    @PostMapping("/fault-mode/reset")
    public ResponseEntity<FaultModeResponse> resetFaultMode() {
        return ResponseEntity.ok(faultSimulationService.resetFaultMode());
    }
}
