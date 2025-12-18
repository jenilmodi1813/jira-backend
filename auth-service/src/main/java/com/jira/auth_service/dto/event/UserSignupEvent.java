package com.jira.auth_service.dto.event;

import java.util.UUID;

public record UserSignupEvent(
        UUID userId,
        String email,
        String verificationToken
) {
}
