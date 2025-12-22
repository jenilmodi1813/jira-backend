package com.jira.auth_service.controller;

import com.jira.auth_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth/admin")
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Promote existing USER → ADMIN
     */
    @PostMapping("/promote/{userId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> promoteToAdmin(
            @PathVariable UUID userId) {

        authService.promoteToAdmin(userId);
        return ResponseEntity.ok("User promoted to ADMIN");
    }

    /**
     * Create ADMIN directly (optional)
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> createAdmin(
            @RequestParam String email) {

        authService.createAdmin(email);
        return ResponseEntity.ok("Admin created");
    }
}