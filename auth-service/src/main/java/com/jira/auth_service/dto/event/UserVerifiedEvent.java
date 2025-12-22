package com.jira.auth_service.dto.event;

import java.util.UUID;

public record UserVerifiedEvent(
        UUID userId,
        String email
) {
}
