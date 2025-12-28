package com.jira.project_service.service;

import com.jira.project_service.dto.request.CreateProjectRequest;
import com.jira.project_service.dto.request.UpdateProjectRequest;
import com.jira.project_service.dto.response.ProjectResponse;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    ProjectResponse create(CreateProjectRequest request, UUID userId);

    ProjectResponse getById(UUID projectId);

    List<ProjectResponse> getByOrganization(UUID organizationId);

    ProjectResponse update(UUID projectId, UpdateProjectRequest request);

    void delete(UUID projectId);

    List<ProjectResponse> getByLead(UUID leadId);
}
