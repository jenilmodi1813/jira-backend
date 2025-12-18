package com.jira.auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyLoginOtpRequest(
        @NotBlank String email,
        @NotBlank String otp
) {
}
