package com.gitopscontrolcenter.controller;

import com.gitopscontrolcenter.dto.ApplicationResponse;
import com.gitopscontrolcenter.service.interfaces.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApplicationResponse> getApplicationByName(@PathVariable String name) {
        return ResponseEntity.ok(applicationService.getApplicationByName(name));
    }
}
