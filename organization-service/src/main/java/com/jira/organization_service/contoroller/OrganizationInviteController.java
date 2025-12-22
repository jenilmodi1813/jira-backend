package com.jira.organization_service.contoroller;

import com.jira.organization_service.annotation.OrgAdmin;
import com.jira.organization_service.dtos.request.InviteUserRequest;
import com.jira.organization_service.service.OrganizationInviteService;
import com.jira.organization_service.service.impl.OrganizationInviteServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationInviteController {

    private final OrganizationInviteService service;

    public OrganizationInviteController(OrganizationInviteService service) {
        this.service = service;
    }

    @PostMapping("/{orgId}/invite")
    @OrgAdmin
    public void invite(
            @PathVariable UUID orgId,
            @RequestBody InviteUserRequest req,
            HttpServletRequest request) {

        UUID userId = (UUID) request.getAttribute("authUserId");
        service.inviteUser(orgId, req, userId);
    }

    @PostMapping("/invites/accept")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> accept(
            @RequestParam String token,
            HttpServletRequest request) {

        UUID userId = (UUID) request.getAttribute("authUserId");
        System.out.println(" authUserId = " + userId);
        if (userId == null) {
            throw new RuntimeException("Unauthorized: userId missing");
        }

        service.acceptInvite(token, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/invites/reject")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> reject(
            @RequestParam String token,
            HttpServletRequest request) {

        UUID userId = (UUID) request.getAttribute("authUserId");
        System.out.println(" authUserId = " + userId);
        if (userId == null) {
            throw new RuntimeException("Unauthorized: userId missing");
        }

        service.rejectInvite(token, userId);
        return ResponseEntity.ok().build();
    }

}
