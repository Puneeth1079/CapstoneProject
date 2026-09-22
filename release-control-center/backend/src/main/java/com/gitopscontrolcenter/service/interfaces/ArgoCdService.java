package com.gitopscontrolcenter.service.interfaces;

import com.gitopscontrolcenter.dto.GitOpsStatusResponse;

import java.util.List;

public interface ArgoCdService {
    List<GitOpsStatusResponse> getGitOpsApplications();
    GitOpsStatusResponse getGitOpsApplication(String name);
}
