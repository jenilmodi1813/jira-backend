package com.jira.issue_service.controller;

import com.jira.issue_service.dto.request.issueType.request.CreateIssueTypeRequest;
import com.jira.issue_service.dto.response.issueType.response.IssueTypeResponse;
import com.jira.issue_service.service.IssueTypeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/issue-types")
//@PreAuthorize("hasRole('ADMIN')")
public class IssueTypeController {

    private final IssueTypeService service;

    public IssueTypeController(IssueTypeService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public IssueTypeResponse create(@Valid @RequestBody CreateIssueTypeRequest request) {
        return service.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<IssueTypeResponse> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
