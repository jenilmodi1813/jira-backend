package com.jira.board_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateBoardRequest(
        @NotBlank String name
) {
}
