package com.gitopscontrolcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityEventResponse {

    private Long id;
    private String eventType;
    private String title;
    private String description;
    private String applicationName;
    private String severity;
    private ZonedDateTime timestamp;
}
