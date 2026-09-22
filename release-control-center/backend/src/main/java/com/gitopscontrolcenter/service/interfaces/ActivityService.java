package com.gitopscontrolcenter.service.interfaces;

import com.gitopscontrolcenter.dto.ActivityEventResponse;

import java.util.List;

public interface ActivityService {
    List<ActivityEventResponse> getRecentActivity();
    void recordActivity(String eventType, String title, String description, String appName, String severity);
}
