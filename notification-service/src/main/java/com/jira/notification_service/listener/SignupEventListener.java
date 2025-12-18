package com.jira.notification_service.listener;

import com.jira.notification_service.dto.event.UserSignupEvent;
import com.jira.notification_service.service.EmailService;
//import jakarta.mail.Message;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SignupEventListener {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public SignupEventListener(EmailService emailService, ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "email.signup.queue")
    public void handleSignup(byte[] message) throws Exception {

        UserSignupEvent event =
                objectMapper.readValue(message, UserSignupEvent.class);

        emailService.sendSignupVerificationEmail(
                event.email(),
                event.verificationToken()
        );

        System.out.println("✅ Signup email sent to: " + event.email());
    }
}