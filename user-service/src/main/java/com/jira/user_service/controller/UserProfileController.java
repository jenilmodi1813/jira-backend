package com.jira.user_service.controller;

import com.jira.user_service.dtos.request.UpdateAvatarRequest;
import com.jira.user_service.dtos.request.UpdateFullNameRequest;
import com.jira.user_service.dtos.request.UpdateTimeZoneRequest;
import com.jira.user_service.dtos.response.UserProfileResponse;
import com.jira.user_service.service.UserProfileService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /**
     * USER / ADMIN
     * Get own profile
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(HttpServletRequest request) {

        UUID authUserId = (UUID) request.getAttribute("authUserId");
        System.out.println("Auther_Id:: "+authUserId);
        return ResponseEntity.ok(
                userProfileService.findById(authUserId)
        );
    }

    /**
     * USER / ADMIN
     * Update own full name
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PatchMapping("/full-name")
    public ResponseEntity<UserProfileResponse> updateFullName(
            @RequestBody UpdateFullNameRequest request,
            HttpServletRequest httpRequest) {

        UUID authUserId = (UUID) httpRequest.getAttribute("authUserId");

        return ResponseEntity.ok(
                userProfileService.updateFullName(authUserId, request.fullName())
        );
    }

    /**
     * USER / ADMIN
     * Update own avatar
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PatchMapping("/avatar")
    public ResponseEntity<UserProfileResponse> updateAvatar(
            @RequestBody UpdateAvatarRequest request,
            HttpServletRequest httpRequest) {

        UUID authUserId = (UUID) httpRequest.getAttribute("authUserId");

        return ResponseEntity.ok(
                userProfileService.updateAvatar(authUserId, request.avatarUrl())
        );
    }

    /**
     * USER / ADMIN
     * Update own timezone
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PatchMapping("/timezone")
    public ResponseEntity<UserProfileResponse> updateTimeZone(
            @RequestBody UpdateTimeZoneRequest request,
            HttpServletRequest httpRequest) {

        UUID authUserId = (UUID) httpRequest.getAttribute("authUserId");

        return ResponseEntity.ok(
                userProfileService.updateTimeZone(authUserId, request.timeZone())
        );
    }

    /**
     * ADMIN ONLY
     * Internal / admin lookup
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{authUserId}")
    public ResponseEntity<UserProfileResponse> getProfileById(
            @PathVariable UUID authUserId) {

        return ResponseEntity.ok(
                userProfileService.findById(authUserId)
        );
    }
    /**
     * INTERNAL API
     * Find user profile by email (used by Organization Service)
     */
//    @PreAuthorize("hasRole('ADMIN')")
    @PermitAll
    @GetMapping("/find-by-email")
    public ResponseEntity<UserProfileResponse> findByEmail(
            @RequestParam String email) {

        return ResponseEntity.ok(
                userProfileService.findByEmail(email)
        );
    }
}