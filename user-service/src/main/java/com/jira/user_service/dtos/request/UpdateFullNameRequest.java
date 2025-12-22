package com.jira.user_service.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateFullNameRequest(@NotBlank String fullName) {
}
