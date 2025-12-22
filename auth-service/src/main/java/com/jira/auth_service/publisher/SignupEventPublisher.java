package com.jira.auth_service.publisher;

import com.jira.auth_service.Config.RabbitConfig;
import com.jira.auth_service.dto.event.UserSignupEvent;
import com.jira.auth_service.dto.event.UserVerifiedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class SignupEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public SignupEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(UserSignupEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.AUTH_EXCHANGE,
                RabbitConfig.SIGNUP_KEY,
                event
        );
    }

    //  NEW (user service)
    public void publishUserVerified(UserVerifiedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.AUTH_EXCHANGE,
                RabbitConfig.USER_VERIFIED_KEY,
                event
        );
    }
}
