package com.jira.issue_service.dto.response.board;

import java.util.UUID;

public record BoardColumnResponse(
        UUID id,
        String name,
        Integer position,
        Boolean isDone,
        UUID boardId
) {
}
