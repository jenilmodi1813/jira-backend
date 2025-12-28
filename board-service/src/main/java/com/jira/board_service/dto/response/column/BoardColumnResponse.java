package com.jira.board_service.dto.response.column;

import com.jira.board_service.entity.Board;

import java.util.UUID;

public record BoardColumnResponse(
        UUID id,
        String name,
        Integer position,
        Boolean isDone,
        UUID boardId
) {
}
