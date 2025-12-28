package com.jira.issue_service.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MoveIssueRequest(
        @NotNull UUID columnId
) {
}
