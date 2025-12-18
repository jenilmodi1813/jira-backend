package com.jira.auth_service.entity;

import com.jira.auth_service.constance.OtpPurpose;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_otp")
public class EmailOtp {

    @Id
    @GeneratedValue
    private UUID id;

    private String email;

    private String otp;

    private LocalDateTime expiresAt;

    private boolean used;

    @Enumerated(EnumType.STRING)
    private OtpPurpose purpose; // SIGNUP, LOGIN
}
