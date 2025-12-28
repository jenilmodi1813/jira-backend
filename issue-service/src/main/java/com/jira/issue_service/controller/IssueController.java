package com.jira.issue_service.controller;

import com.jira.issue_service.dto.request.CreateIssueRequest;
import com.jira.issue_service.dto.request.CreateSubTaskRequest;
import com.jira.issue_service.dto.request.MoveIssueRequest;
import com.jira.issue_service.dto.request.UpdateIssueRequest;
import com.jira.issue_service.dto.response.IssueResponse;
import com.jira.issue_service.service.IssueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService service;

    public IssueController(IssueService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public IssueResponse create(
            @Valid @RequestBody CreateIssueRequest request,
            HttpServletRequest http) {

        UUID userId = (UUID) http.getAttribute("authUserId");
        return service.create(request, userId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public IssueResponse get(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<IssueResponse> byProject(@PathVariable UUID projectId) {
        return service.getByProject(projectId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public IssueResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateIssueRequest request) {

        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PostMapping("/{parentIssueId}/subtasks")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public IssueResponse createSubTask(
            @PathVariable UUID parentIssueId,
            @Valid @RequestBody CreateSubTaskRequest request) {

        return service.createSubTask(parentIssueId, request);
    }

    @PatchMapping("/{id}/move")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public IssueResponse moveIssue(
            @PathVariable UUID id,
            @RequestBody MoveIssueRequest request) {

        return service.move(id, request.columnId());
    }
}


