package com.jira.board_service.dto.request.column;

import java.util.UUID;

public record ReorderColumnRequest(
        UUID columnId,
        Integer newPosition
) {
}
