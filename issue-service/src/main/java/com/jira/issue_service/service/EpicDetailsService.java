package com.jira.issue_service.service;

import com.jira.issue_service.dto.request.epicDetails.request.CreateEpicDetailsRequest;
import com.jira.issue_service.dto.response.epicDetails.response.EpicDetailsResponse;

import java.util.UUID;

public interface EpicDetailsService {

    EpicDetailsResponse create(CreateEpicDetailsRequest request);
    EpicDetailsResponse get(UUID epicId);
    EpicDetailsResponse update(UUID epicId, CreateEpicDetailsRequest request);

}
