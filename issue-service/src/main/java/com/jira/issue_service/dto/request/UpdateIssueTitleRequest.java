package com.jira.issue_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateIssueTitleRequest(
        @NotBlank String title
) {
}
