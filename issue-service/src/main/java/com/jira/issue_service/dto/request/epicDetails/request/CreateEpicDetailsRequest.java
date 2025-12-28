package com.jira.issue_service.dto.request.epicDetails.request;

import jakarta.validation.constraints.NotBlank;

public record CreateEpicDetailsRequest(
        @NotBlank String epicName,
        String epicColor
) {
}
