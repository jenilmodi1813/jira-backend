package com.jira.auth_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record IdentifyRequest(
        @Email @NotBlank String email) {
}
