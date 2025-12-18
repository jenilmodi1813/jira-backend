package com.jira.auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyEmailRequest(
        @NotBlank String otp
//        @NotBlank String password
) {
}
