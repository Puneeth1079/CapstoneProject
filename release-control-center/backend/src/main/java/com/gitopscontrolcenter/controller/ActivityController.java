package com.gitopscontrolcenter.controller;

import com.gitopscontrolcenter.dto.ActivityEventResponse;
import com.gitopscontrolcenter.service.interfaces.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<List<ActivityEventResponse>> getRecentActivity() {
        return ResponseEntity.ok(activityService.getRecentActivity());
    }
}
