package com.jira.project_service.dto.request;

import java.util.UUID;

public record UpdateProjectRequest(
        String name,
        String projectType,
        UUID leadId
) {
}
