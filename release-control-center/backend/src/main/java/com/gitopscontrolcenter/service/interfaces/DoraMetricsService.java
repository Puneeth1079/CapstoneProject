package com.gitopscontrolcenter.service.interfaces;

import com.gitopscontrolcenter.dto.DoraMetricsResponse;

public interface DoraMetricsService {
    DoraMetricsResponse getDoraMetrics(String period);
    void calculateAndSnapshotDoraMetrics();
}
