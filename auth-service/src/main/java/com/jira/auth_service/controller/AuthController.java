package com.jira.auth_service.controller;

import com.jira.auth_service.dto.request.*;
import com.jira.auth_service.dto.response.AuthResponse;
import com.jira.auth_service.dto.response.IdentifyResponse;
import com.jira.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * STEP 1: Email identify (Jira-style)
     * UI calls this first
     */
    @PostMapping("/identify")
    public ResponseEntity<IdentifyResponse> identify(
            @Valid @RequestBody IdentifyRequest request) {

        return ResponseEntity.ok(authService.identify(request));
    }

    /**
     * STEP 2: Signup (email only)
     */
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
    }

    /**
     * STEP 3: Verify email + set password
     */
    @PostMapping("/verify-email")
    public void verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request);
    }

    /**
     * STEP 4: Login (only after verification)
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * STEP 5: Refresh JWT
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(authService.refreshToken(request));
    }
    /**
     * STEP 6: Logout
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String authHeader) {
        // Expecting "Bearer <accessToken>"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        authService.logout(token);
    }

    @PostMapping("/verify-login")
    public ResponseEntity<AuthResponse> verifyLogin(
            @Valid @RequestBody VerifyLoginOtpRequest request) {

        return ResponseEntity.ok(authService.verifyLoginOtp(request));
    }
}
