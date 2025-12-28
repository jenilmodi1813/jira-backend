package com.jira.issue_service.service;

import com.jira.issue_service.dto.request.issueType.request.CreateIssueTypeRequest;
import com.jira.issue_service.dto.response.issueType.response.IssueTypeResponse;

import java.util.List;
import java.util.UUID;

public interface IssueTypeService {
    IssueTypeResponse create(CreateIssueTypeRequest request);
    List<IssueTypeResponse> getAll();
    void delete(UUID id);
}
