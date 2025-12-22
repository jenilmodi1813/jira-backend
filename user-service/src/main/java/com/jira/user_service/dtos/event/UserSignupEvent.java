package com.jira.user_service.dtos.event;

import java.util.UUID;

public record UserSignupEvent(
        UUID userId,
        String email,
        String verificationToken
) {
}
