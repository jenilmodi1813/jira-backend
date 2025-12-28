package com.jira.board_service.dto.response;

import com.jira.board_service.constance.BoardType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BoardResponse(
        UUID id,
        UUID projectId,
        String name,
        BoardType type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<BoardColumnResponse> columns
) {
}
