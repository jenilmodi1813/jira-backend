package com.jira.organization_service.dtos.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationMemberResponse(
        UUID userId,
        String email,
        String displayName,
        String avatarUrl,
        String orgRole,
        String jobTitle,
        String department,
        LocalDateTime joinedAt
) {
}
