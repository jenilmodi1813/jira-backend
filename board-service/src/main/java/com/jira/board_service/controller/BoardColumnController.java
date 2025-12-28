package com.jira.board_service.controller;

import com.jira.board_service.dto.request.UpdateBoardRequest;
import com.jira.board_service.dto.request.column.CreateColumnRequest;
import com.jira.board_service.dto.request.column.ReorderColumnRequest;
import com.jira.board_service.dto.request.column.UpdateColumnRequest;
import com.jira.board_service.dto.response.BoardColumnResponse;
import com.jira.board_service.service.BoardColumnService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/boards/{boardId}/columns")
public class BoardColumnController {

    private final BoardColumnService service;

    public BoardColumnController(BoardColumnService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BoardColumnResponse create(
            @PathVariable UUID boardId,
            @Valid @RequestBody CreateColumnRequest request) {

        return service.create(boardId, request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<BoardColumnResponse> getAll(@PathVariable UUID boardId) {
        return service.getByBoard(boardId);
    }

    @PutMapping("/{columnId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BoardColumnResponse update(
            @PathVariable UUID columnId,
            @Valid @RequestBody UpdateColumnRequest request) {

        return service.update(columnId, request);
    }

    @DeleteMapping("/{columnId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID columnId) {
        service.delete(columnId);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/reorder")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> reorder(
            @PathVariable UUID boardId,
            @RequestBody ReorderColumnRequest req) {

        service.reorder(boardId, req.columnId(), req.newPosition());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{columnId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public com.jira.board_service.dto.response.column.BoardColumnResponse getById(@PathVariable UUID columnId) {
        return service.getById(columnId);
    }
}


