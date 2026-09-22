package com.gitopscontrolcenter.service.interfaces;

import com.gitopscontrolcenter.dto.ApplicationResponse;

import java.util.List;

public interface ApplicationService {
    List<ApplicationResponse> getAllApplications();
    ApplicationResponse getApplicationByName(String name);
}
