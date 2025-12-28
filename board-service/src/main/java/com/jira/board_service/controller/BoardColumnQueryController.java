package com.jira.board_service.controller;

import com.jira.board_service.dto.response.column.BoardColumnResponse;
import com.jira.board_service.service.BoardColumnService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/board-columns")
public class BoardColumnQueryController {

    private final BoardColumnService service;

    public BoardColumnQueryController(BoardColumnService service) {
        this.service = service;
    }

    @GetMapping("/{columnId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BoardColumnResponse getById(@PathVariable UUID columnId) {
        return service.getById(columnId);
    }
}