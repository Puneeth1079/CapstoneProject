package com.gitopscontrolcenter.service.implementation;

import com.gitopscontrolcenter.dto.ActivityEventResponse;
import com.gitopscontrolcenter.entity.ActivityEvent;
import com.gitopscontrolcenter.repository.ActivityEventRepository;
import com.gitopscontrolcenter.service.interfaces.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityEventRepository activityEventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ActivityEventResponse> getRecentActivity() {
        return activityEventRepository.findTop30ByOrderByTimestampDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void recordActivity(String eventType, String title, String description, String appName, String severity) {
        ActivityEvent event = ActivityEvent.builder()
                .eventType(eventType)
                .title(title)
                .description(description)
                .applicationName(appName)
                .severity(severity != null ? severity : "INFO")
                .timestamp(ZonedDateTime.now())
                .build();
        activityEventRepository.save(event);
    }

    private ActivityEventResponse mapToResponse(ActivityEvent event) {
        return ActivityEventResponse.builder()
                .id(event.getId())
                .eventType(event.getEventType())
                .title(event.getTitle())
                .description(event.getDescription())
                .applicationName(event.getApplicationName())
                .severity(event.getSeverity())
                .timestamp(event.getTimestamp())
                .build();
    }
}
