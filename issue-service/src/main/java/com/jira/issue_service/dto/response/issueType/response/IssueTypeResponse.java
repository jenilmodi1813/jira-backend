package com.jira.issue_service.dto.response.issueType.response;

import java.util.UUID;

public record IssueTypeResponse(
        UUID id,
        String name
) {
}
