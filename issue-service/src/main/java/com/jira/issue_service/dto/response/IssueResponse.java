package com.jira.issue_service.dto.response;

import com.jira.issue_service.entity.Issue;

import java.time.LocalDateTime;
import java.util.UUID;

public record IssueResponse(
        UUID id,
        UUID projectId,
        UUID issueTypeId,
        UUID boardColumnId,
        Issue parentIssueId,
        UUID epicId,
        String title,
        String description,
        String status,
        String priority,
        UUID assigneeId,
        UUID reporterId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
