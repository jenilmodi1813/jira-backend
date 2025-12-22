package com.jira.organization_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ORG_EXCHANGE = "organization.exchange";
    public static final String INVITE_QUEUE = "organization.invite.queue";
    public static final String SIGNUP_KEY = "org.signup";

    @Bean
    public TopicExchange organizationExchange() {
        return new TopicExchange(ORG_EXCHANGE);
    }

    @Bean
    public Queue organizationQueue() {
        return new Queue(INVITE_QUEUE, true);
    }

    @Bean
    public Binding signupBinding() {
        return BindingBuilder
                .bind(organizationQueue())
                .to(organizationExchange())
                .with(SIGNUP_KEY);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
