package com.jira.notification_service.service;

public interface EmailService {
    void sendSignupVerificationEmail(String to, String token);

    void sendOrganizationInviteEmail(String email, String link);
}
