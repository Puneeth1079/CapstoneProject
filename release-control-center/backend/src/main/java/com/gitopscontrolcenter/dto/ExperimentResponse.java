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
public class ExperimentResponse {

    private Long id;
    private String mode;
    private String description;
    private Boolean active;
    private ZonedDateTime startedAt;
    private ZonedDateTime endedAt;
    private String status;
}
