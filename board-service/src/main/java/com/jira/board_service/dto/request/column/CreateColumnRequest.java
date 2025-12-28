package com.jira.board_service.dto.request.column;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateColumnRequest(
        @NotBlank String name,
        @NotNull Integer position,
        @NotNull Boolean isDone
) {
}
