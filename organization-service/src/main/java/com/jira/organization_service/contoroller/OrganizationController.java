package com.jira.organization_service.contoroller;

import com.jira.organization_service.dtos.request.CreateOrganizationRequest;
import com.jira.organization_service.dtos.response.OrganizationResponse;
import com.jira.organization_service.service.OrganizationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService service;

    public OrganizationController(OrganizationService service) {
        this.service = service;
    }

    @PostMapping
    public OrganizationResponse create(
            @RequestBody CreateOrganizationRequest req,
            HttpServletRequest request) {

        UUID userId = (UUID) request.getAttribute("authUserId");
        return service.createOrganization(req, userId);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<OrganizationResponse> myOrgs(HttpServletRequest request) {

        UUID userId = (UUID) request.getAttribute("authUserId");
        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        return service.myOrganizations(userId);
    }
}
