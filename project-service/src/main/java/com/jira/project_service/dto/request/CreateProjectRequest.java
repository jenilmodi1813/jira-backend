package com.jira.project_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateProjectRequest(
        @NotNull
        UUID organizationId,

        @NotBlank
        String name,

        @NotBlank
        @Size(max = 10)
        String projectKey,

        @NotBlank
        String projectType,

        UUID leadId

) {
}
