package com.gitopscontrolcenter.service.interfaces;

import com.gitopscontrolcenter.dto.ExperimentRequest;
import com.gitopscontrolcenter.dto.ExperimentResponse;

public interface ReleaseExperimentService {
    ExperimentResponse triggerExperiment(ExperimentRequest request);
    ExperimentResponse getActiveExperiment();
    ExperimentResponse resetExperiment();
}
