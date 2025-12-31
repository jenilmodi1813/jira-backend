package com.jira.issue_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateIssueDescriptionRequest(
        @NotBlank String description
) {
}
