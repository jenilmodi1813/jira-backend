package com.jira.board_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateBoardRequest(
        @NotNull UUID projectId,
        @NotBlank String name,
        @NotNull String type
) {
}
