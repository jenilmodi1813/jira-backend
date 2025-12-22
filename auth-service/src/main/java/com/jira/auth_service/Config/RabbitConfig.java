package com.jira.auth_service.Config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
//import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Binding;

@Configuration
public class RabbitConfig {

    public static final String AUTH_EXCHANGE = "auth.exchange";
    public static final String SIGNUP_QUEUE = "email.signup.queue";
    public static final String SIGNUP_KEY = "auth.signup";
    // Queue for User Service (new)
    public static final String USER_QUEUE = "user.signup.queue";

    //  NEW (for user-service)
    public static final String USER_VERIFIED_KEY = "auth.verified";

    @Bean
    public TopicExchange authExchange() {
        return new TopicExchange(AUTH_EXCHANGE);
    }

    @Bean
    public Queue signupQueue() {
        return new Queue(SIGNUP_QUEUE, true);
    }

    // New queue for user service
    @Bean
    public Queue userQueue() {
        return new Queue(USER_QUEUE, true);
    }

    @Bean
    public Binding signupBinding() {
        return BindingBuilder
                .bind(signupQueue())
                .to(authExchange())
                .with(SIGNUP_KEY);
    }

    @Bean
    public Binding userBinding() {
        return BindingBuilder
                .bind(userQueue())
                .to(authExchange())
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
