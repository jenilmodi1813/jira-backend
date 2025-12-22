package com.jira.organization_service.dtos.response;

import java.util.UUID;

public record OrganizationResponse(
        UUID id,
        String name,
        UUID ownerId
) {
}
