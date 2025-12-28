package com.jira.issue_service.dto.request.issueType.request;

import jakarta.validation.constraints.NotBlank;

public record CreateIssueTypeRequest(
        @NotBlank String name
) {
}
