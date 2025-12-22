package com.jira.notification_service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jira.notification_service.dto.event.OrganizationInviteEvent;
import com.jira.notification_service.dto.event.UserSignupEvent;
import com.jira.notification_service.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrganizationInviteListener {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public OrganizationInviteListener(
            EmailService emailService,
            ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "organization.invite.queue")
    public void handleInvite(byte[] message) throws Exception {

        OrganizationInviteEvent event =
                objectMapper.readValue(message, OrganizationInviteEvent.class);

        String link =
                "http://localhost:3000/accept-invite?token=" + event.inviteToken();

        emailService.sendOrganizationInviteEmail(
                event.email(),
                link
        );

        System.out.println("✅ Organization invite email sent to: " + event.email());
    }
}