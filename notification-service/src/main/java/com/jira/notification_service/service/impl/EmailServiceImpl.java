package com.jira.notification_service.service.impl;

import com.jira.notification_service.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendSignupVerificationEmail(String to, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Jira verification code");
        message.setText("""
        Welcome to Jira 👋

        Your verification code is:

        %s

        This code will expire in 10 minutes.

        If you didn’t request this, you can safely ignore this email.
        """.formatted(otp)
        );

        mailSender.send(message);
    }
    @Override
    public void sendOrganizationInviteEmail(String to, String inviteLink) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("You're invited to join a Jira organization");
        message.setText("""
        Hello 👋

        You’ve been invited to join a Jira organization.

        Click below to accept the invitation:
        %s

        This invite will expire in 48 hours.
        """.formatted(inviteLink));

        mailSender.send(message);
    }
}