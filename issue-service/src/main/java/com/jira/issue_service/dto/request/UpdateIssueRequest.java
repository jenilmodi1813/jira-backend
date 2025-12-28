package com.jira.issue_service.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UpdateIssueRequest(
        @NotBlank
        String title,
        String description,
        String priority,
        String status,
        UUID assigneeId,
        UUID boardColumnId
) {
}
