package com.cloudcart.service.interfaces;

import com.cloudcart.dto.FaultModeRequest;
import com.cloudcart.dto.FaultModeResponse;

public interface FaultSimulationService {
    FaultModeResponse setFaultMode(FaultModeRequest request);
    FaultModeResponse getFaultMode();
    FaultModeResponse resetFaultMode();
    void evaluateAndApplyFault();
    boolean shouldFailTransaction();
}
