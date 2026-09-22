package com.gitopscontrolcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricResponse {

    private String name;
    private String currentValue;
    private String threshold;
    private String status; // PASS, FAIL, WARNING
    private String trend;  // STABLE, IMPROVING, DEGRADING
    private List<Map<String, Object>> timeSeries;
    private ZonedDateTime lastChecked;
}
