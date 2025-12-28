package com.jira.project_service.controller;

import com.jira.project_service.dto.request.CreateProjectRequest;
import com.jira.project_service.dto.request.UpdateProjectRequest;
import com.jira.project_service.dto.response.ProjectResponse;
import com.jira.project_service.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ProjectResponse create(
            @RequestBody @Valid CreateProjectRequest request,
            HttpServletRequest http) {

        UUID userId = (UUID) http.getAttribute("authUserId");
        return service.create(request, userId);
    }

//    @GetMapping("/{projectId}")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
//    public ProjectResponse get(@PathVariable UUID projectId) {
//        return service.getById(projectId);
//    }

    @GetMapping("/organization/{orgId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProjectResponse> byOrg(@PathVariable UUID orgId) {
        return service.getByOrganization(orgId);
    }

    @PutMapping("/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProjectResponse update(
            @PathVariable UUID projectId,
            @RequestBody UpdateProjectRequest request) {

        return service.update(projectId, request);
    }

    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID projectId) {
        service.delete(projectId);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProjectResponse> myProjects(HttpServletRequest http) {
        UUID userId = (UUID) http.getAttribute("authUserId");
        return service.getByLead(userId);
    }


    @GetMapping("/lead/{leadId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProjectResponse> byLead(@PathVariable UUID leadId) {
        return service.getByLead(leadId);
    }

}