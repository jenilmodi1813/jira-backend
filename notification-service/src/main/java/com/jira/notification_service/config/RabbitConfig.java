package com.jira.notification_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
//import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String SIGNUP_QUEUE = "email.signup.queue";
    public static final String ORG_EXCHANGE = "organization.exchange";
    public static final String INVITE_QUEUE = "organization.invite.queue";

    @Bean
    public Queue signupQueue() {
        return new Queue(SIGNUP_QUEUE, true);
    }

    @Bean
    public Queue inviteQueue() {
        return new Queue(INVITE_QUEUE,true);
    }

    @Bean
    public TopicExchange orgExchange() {
        return new TopicExchange(ORG_EXCHANGE);
    }

    @Bean
    public Binding inviteBinding() {
        return BindingBuilder
                .bind(inviteQueue())
                .to(orgExchange())
                .with("organization.invite.created");
    }

}