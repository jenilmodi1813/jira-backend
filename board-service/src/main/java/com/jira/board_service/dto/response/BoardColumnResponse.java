package com.jira.board_service.dto.response;

import java.util.UUID;

public record BoardColumnResponse(
        UUID id,
        String name,
        Integer position,
        Boolean isDone
) {
}
