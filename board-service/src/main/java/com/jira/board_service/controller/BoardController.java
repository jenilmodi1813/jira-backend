package com.jira.board_service.controller;

import com.jira.board_service.dto.request.CreateBoardRequest;
import com.jira.board_service.dto.request.UpdateBoardRequest;
import com.jira.board_service.dto.response.BoardResponse;
import com.jira.board_service.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService service;

    public BoardController(BoardService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<BoardResponse> create(
            @Valid @RequestBody CreateBoardRequest request,
            HttpServletRequest http) {

        UUID userId = (UUID) http.getAttribute("authUserId");
        System.out.println("user ID : "+userId);
        return ResponseEntity.ok(service.create(request, userId));
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BoardResponse getByProject(@PathVariable UUID projectId) {
        return service.getByProject(projectId);
    }

    @PutMapping("/{boardId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BoardResponse update(
            @PathVariable UUID boardId,
            @Valid @RequestBody UpdateBoardRequest request) {
        return service.update(boardId, request);
    }

    @DeleteMapping("/{boardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID boardId) {
        service.delete(boardId);
        return ResponseEntity.noContent().build();
    }
}
