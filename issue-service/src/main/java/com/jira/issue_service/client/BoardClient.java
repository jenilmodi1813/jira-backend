package com.jira.issue_service.client;

import com.jira.issue_service.dto.response.board.BoardColumnResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "board-service")
public interface BoardClient {
    @GetMapping("/api/boards/{boardId}/columns/{columnId}")
    BoardColumnResponse getColumn(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId
    );

    @GetMapping("api/board-columns/{columnId}")
    BoardColumnResponse getById(@PathVariable UUID columnId);
}
