package com.jira.issue_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateIssueRequest(
        @NotNull UUID projectId,
        @NotNull UUID issueTypeId,

        UUID boardColumnId,
//        UUID parentIssueId,
        UUID epicId,

        @NotBlank String title,
        String description,

        String priority,
        UUID assigneeId

) {
}
