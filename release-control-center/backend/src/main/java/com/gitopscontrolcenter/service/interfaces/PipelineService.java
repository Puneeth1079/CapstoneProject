package com.gitopscontrolcenter.service.interfaces;

import com.gitopscontrolcenter.dto.PipelineResponse;

import java.util.List;

public interface PipelineService {
    List<PipelineResponse> getPipelineRuns();
}
