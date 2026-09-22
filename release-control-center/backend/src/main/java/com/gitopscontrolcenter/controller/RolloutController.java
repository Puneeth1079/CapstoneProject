package com.gitopscontrolcenter.controller;

import com.gitopscontrolcenter.dto.RolloutResponse;
import com.gitopscontrolcenter.service.interfaces.RolloutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rollouts")
@RequiredArgsConstructor
public class RolloutController {

    private final RolloutService rolloutService;

    @GetMapping
    public ResponseEntity<List<RolloutResponse>> getAllRollouts() {
        return ResponseEntity.ok(rolloutService.getAllRollouts());
    }

    @GetMapping("/{application}")
    public ResponseEntity<RolloutResponse> getRolloutByApplication(@PathVariable String application) {
        return ResponseEntity.ok(rolloutService.getRolloutByApplication(application));
    }

    @PostMapping("/{application}/promote")
    public ResponseEntity<RolloutResponse> promoteRollout(@PathVariable String application) {
        return ResponseEntity.ok(rolloutService.promoteRollout(application));
    }

    @PostMapping("/{application}/pause")
    public ResponseEntity<RolloutResponse> pauseRollout(@PathVariable String application) {
        return ResponseEntity.ok(rolloutService.pauseRollout(application));
    }

    @PostMapping("/{application}/resume")
    public ResponseEntity<RolloutResponse> resumeRollout(@PathVariable String application) {
        return ResponseEntity.ok(rolloutService.resumeRollout(application));
    }

    @PostMapping("/{application}/abort")
    public ResponseEntity<RolloutResponse> abortRollout(@PathVariable String application) {
        return ResponseEntity.ok(rolloutService.abortRollout(application));
    }

    @PostMapping("/{application}/retry")
    public ResponseEntity<RolloutResponse> retryRollout(@PathVariable String application) {
        return ResponseEntity.ok(rolloutService.retryRollout(application));
    }
}
