package com.jira.auth_service.service.impl;

import com.jira.auth_service.dto.event.UserSignupEvent;
import com.jira.auth_service.dto.request.*;
import com.jira.auth_service.dto.response.AuthResponse;
import com.jira.auth_service.dto.response.IdentifyResponse;
import com.jira.auth_service.entity.EmailVerificationToken;
import com.jira.auth_service.entity.RefreshToken;
import com.jira.auth_service.entity.User;
import com.jira.auth_service.publisher.SignupEventPublisher;
import com.jira.auth_service.exception.BadRequestException;
import com.jira.auth_service.exception.UnauthorizedException;
import com.jira.auth_service.repository.EmailVerificationTokenRepository;
import com.jira.auth_service.repository.RefreshTokenRepository;
import com.jira.auth_service.repository.UserRepository;
import com.jira.auth_service.security.JwtService;
import com.jira.auth_service.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final EmailVerificationTokenRepository tokenRepo;
    private final RefreshTokenRepository refreshRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SignupEventPublisher eventPublisher; // Commented for Notification Service

    public AuthServiceImpl(UserRepository userRepo,
                           EmailVerificationTokenRepository tokenRepo,
                           RefreshTokenRepository refreshRepo,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
            SignupEventPublisher eventPublisher) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.refreshRepo = refreshRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
         this.eventPublisher = eventPublisher;
    }

    private String generateOtp() {
        return String.valueOf(
                ThreadLocalRandom.current().nextInt(100000, 999999)
        );
    }

    // Check if user exists
    @Override
    public IdentifyResponse identify(IdentifyRequest request) {
        var userOpt = userRepo.findByEmail(request.email());
        return new IdentifyResponse(
                userOpt.isPresent(),
                userOpt.map(User::isVerified).orElse(false)
        );
    }

    // Signup - Jira style: email first, password set later
    @Override
    public void signup(SignupRequest request) {
        if (userRepo.findByEmail(request.email()).isPresent()) {
            throw new BadRequestException("Email already registered");
        }

        User user = User.builder()
                .email(request.email())
                .isVerified(false)
                .isActive(true)
                .build();

        userRepo.save(user);
        String otp = generateOtp();
        EmailVerificationToken token = EmailVerificationToken.builder()
                .user(user)
                .token(otp)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .used(false)
                .build();

        tokenRepo.save(token);

        // 🔥 PUBLISH EVENT TO RABBITMQ
        eventPublisher.publish(
                new UserSignupEvent(
                        user.getId(),
                        user.getEmail(),
                        otp
                )
        );

        System.out.println("Signup event published for email: " + user.getEmail());
    }

    // Verify email and set password
    @Override
    @Transactional
    public void verifyEmail(VerifyEmailRequest request) {
        log.info("VERIFY EMAIL API HIT for {}", request.otp());
        var token = tokenRepo.findByToken(request.otp())
                .orElseThrow(() -> new BadRequestException("Invalid token"));

        if (token.isUsed() || token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP expired or already used");
        }

        var user = token.getUser();
//        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setVerified(true);
        userRepo.save(user);

        token.setUsed(true);
        tokenRepo.save(token);
    }

    // Login
    @Override
    public AuthResponse login(LoginRequest request) {
        var user = userRepo.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid email"));

        if (!user.isVerified()) {
            throw new UnauthorizedException("Email not verified");
        }

        // 🔐 Generate OTP for login
        String otp = generateOtp();

        EmailVerificationToken token = EmailVerificationToken.builder()
                .user(user)
                .token(otp)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        tokenRepo.save(token);

        // 🔥 Send OTP via RabbitMQ
        eventPublisher.publish(
                new UserSignupEvent(
                        user.getId(),
                        user.getEmail(),
                        otp
                )
        );

        log.info("LOGIN OTP sent to {}", user.getEmail());

        // 🚫 NO JWT YET
        return new AuthResponse(null, null);
    }

    @Override
    @Transactional
    public AuthResponse verifyLoginOtp(VerifyLoginOtpRequest request) {

        var user = userRepo.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        var token = tokenRepo.findByToken(request.otp())
                .orElseThrow(() -> new UnauthorizedException("Invalid OTP"));

        if (token.isUsed() || token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("OTP expired or already used");
        }

        if (!token.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("OTP does not belong to this user");
        }

        token.setUsed(true);
        tokenRepo.save(token);

        //  ISSUE JWT HERE
        String accessToken = jwtService.generateAccessToken(user.getId().toString());
        String refreshToken = jwtService.generateRefreshToken(user.getId().toString());

        refreshRepo.save(
                RefreshToken.builder()
                        .user(user)
                        .token(refreshToken)
                        .expiresAt(LocalDateTime.now().plusDays(7))
                        .revoked(false)
                        .build()
        );

        return new AuthResponse(accessToken, refreshToken);
    }

    // Refresh token
    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        var refreshToken = refreshRepo.findByToken(request.refreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (refreshToken.isRevoked() || refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("Refresh token expired or revoked");
        }

        var user = refreshToken.getUser();
        String newAccessToken = jwtService.generateAccessToken(user.getId().toString());

        return new AuthResponse(newAccessToken, refreshToken.getToken());
    }

    @Override
    public void logout(String accessToken) {
        // Extract user ID from access token
        String userId = jwtService.extractUserId(accessToken);

        // Revoke all refresh tokens for this user
        var user = userRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        var refreshTokens = refreshRepo.findAllByUser(user);
        refreshTokens.forEach(token -> token.setRevoked(true));
        refreshRepo.saveAll(refreshTokens);
    }
}


