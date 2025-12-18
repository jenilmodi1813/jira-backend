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
}