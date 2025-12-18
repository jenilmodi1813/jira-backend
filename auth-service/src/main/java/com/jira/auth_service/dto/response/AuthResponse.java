package com.jira.auth_service.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
