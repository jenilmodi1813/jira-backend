package com.jira.issue_service.dto.response.epicDetails.response;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record EpicDetailsResponse(
        UUID epicId,
        String epicName,
        String epicColor
) {
}
