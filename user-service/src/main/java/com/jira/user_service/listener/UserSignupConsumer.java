package com.jira.user_service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jira.user_service.config.UserRabbitConfig;
import com.jira.user_service.dtos.event.UserSignupEvent;
import com.jira.user_service.service.UserProfileService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
//import tools.jackson.databind.ObjectMapper;

@Service
public class UserSignupConsumer {

    private final UserProfileService userProfileService;
    private final ObjectMapper objectMapper;

    public UserSignupConsumer(UserProfileService userProfileService, ObjectMapper objectMapper) {
        this.userProfileService = userProfileService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = UserRabbitConfig.USER_VERIFIED_QUEUE)
    public void consume(byte[] message) throws Exception{
        System.out.println("User Service received signup: " );
        UserSignupEvent event =
                objectMapper.readValue(message, UserSignupEvent.class);

        // TODO: save UserProfile in DB
        userProfileService.save(
                event.userId(),
                event.email()
        );
        // userProfileService.save(event.getUserId(), event.getEmail());
        System.out.println("✅ User Save in DB : " + event.email());
    }
}
