package com.gitopscontrolcenter.controller;

import com.gitopscontrolcenter.dto.RollbackResponse;
import com.gitopscontrolcenter.service.interfaces.RollbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rollbacks")
@RequiredArgsConstructor
public class RollbackController {

    private final RollbackService rollbackService;

    @GetMapping
    public ResponseEntity<List<RollbackResponse>> getAllRollbacks() {
        return ResponseEntity.ok(rollbackService.getAllRollbacks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RollbackResponse> getRollbackById(@PathVariable Long id) {
        return ResponseEntity.ok(rollbackService.getRollbackById(id));
    }

    @GetMapping("/application/{appName}")
    public ResponseEntity<List<RollbackResponse>> getRollbacksByApplication(@PathVariable String appName) {
        return ResponseEntity.ok(rollbackService.getRollbacksByApplication(appName));
    }
}
