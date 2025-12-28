package com.jira.project_service.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectResponse(
        UUID id,
        UUID organizationId,
        String name,
        String projectKey,
        String projectType,
        UUID leadId,
        LocalDateTime createdAt

) {
}
