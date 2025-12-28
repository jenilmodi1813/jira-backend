package com.jira.issue_service.service;

import com.jira.issue_service.dto.request.CreateIssueRequest;
import com.jira.issue_service.dto.request.CreateSubTaskRequest;
import com.jira.issue_service.dto.request.UpdateIssueRequest;
import com.jira.issue_service.dto.response.IssueResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface IssueService {
    IssueResponse create(CreateIssueRequest request, UUID reporterId);

    IssueResponse getById(UUID issueId);

    List<IssueResponse> getByProject(UUID projectId);

    IssueResponse update(UUID issueId, UpdateIssueRequest request);

    void delete(UUID issueId);

    IssueResponse createSubTask(UUID parentIssueId, @Valid CreateSubTaskRequest request);

    IssueResponse move(UUID id, @NotNull UUID uuid);
}
