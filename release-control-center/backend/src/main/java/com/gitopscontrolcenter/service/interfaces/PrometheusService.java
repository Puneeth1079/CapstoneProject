package com.gitopscontrolcenter.service.interfaces;

import com.gitopscontrolcenter.dto.MetricResponse;

import java.util.List;

public interface PrometheusService {
    List<MetricResponse> getApplicationMetrics(String applicationName);
}
