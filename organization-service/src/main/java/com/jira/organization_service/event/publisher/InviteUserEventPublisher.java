package com.jira.organization_service.event.publisher;

import com.jira.organization_service.config.RabbitConfig;
import com.jira.organization_service.dtos.event.OrganizationInviteEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
@Service
public class InviteUserEventPublisher {

        private final RabbitTemplate rabbitTemplate;

        public InviteUserEventPublisher(RabbitTemplate rabbitTemplate) {
            this.rabbitTemplate = rabbitTemplate;
        }

        public void publish(OrganizationInviteEvent event) {
            rabbitTemplate.convertAndSend(
                    RabbitConfig.ORG_EXCHANGE,
                    RabbitConfig.SIGNUP_KEY,
                    event
            );
        }
}
