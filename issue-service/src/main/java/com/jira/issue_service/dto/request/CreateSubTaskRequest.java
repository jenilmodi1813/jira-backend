package com.jira.issue_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateSubTaskRequest(
        @NotBlank
        String title,

        String description,

        @NotNull
        UUID assigneeId,

        String priority
) {
}
