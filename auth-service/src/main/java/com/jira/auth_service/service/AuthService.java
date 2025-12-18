package com.jira.auth_service.service;

import com.jira.auth_service.dto.request.*;
import com.jira.auth_service.dto.response.AuthResponse;
import com.jira.auth_service.dto.response.IdentifyResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

public interface AuthService {
    IdentifyResponse identify(IdentifyRequest request);
    void signup(SignupRequest request);
    void verifyEmail(VerifyEmailRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout(String token);

    @Nullable AuthResponse verifyLoginOtp(@Valid VerifyLoginOtpRequest request);
}
