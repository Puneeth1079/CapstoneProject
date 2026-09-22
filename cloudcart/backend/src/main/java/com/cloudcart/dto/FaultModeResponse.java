package com.cloudcart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaultModeResponse {

    private String activeMode;
    private double errorRate;
    private long latencyMs;
    private boolean active;
    private String description;
    private ZonedDateTime updatedTime;
}
