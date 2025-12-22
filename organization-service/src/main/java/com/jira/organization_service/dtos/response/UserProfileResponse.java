package com.jira.organization_service.dtos.response;

import java.util.UUID;

public record UserProfileResponse(
        UUID authUserId,
        String fullName,
        String email,
        String avatarUrl,
        String timeZone
) {
}
