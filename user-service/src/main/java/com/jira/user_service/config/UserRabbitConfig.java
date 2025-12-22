package com.jira.user_service.config;




import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Binding;

@Configuration
public class UserRabbitConfig {

    public static final String USER_VERIFIED_QUEUE = "user.verified.queue";
    public static final String USER_VERIFIED_KEY = "auth.verified";
    public static final String AUTH_EXCHANGE = "auth.exchange";

    @Bean
    public Queue userVerifiedQueue() {
        return new Queue(USER_VERIFIED_QUEUE, true);
    }

    @Bean
    public TopicExchange authExchange() {
        return new TopicExchange(AUTH_EXCHANGE);
    }

    @Bean
    public Binding userVerifiedBinding(
            Queue userVerifiedQueue,
            TopicExchange authExchange) {

        return BindingBuilder
                .bind(userVerifiedQueue)
                .to(authExchange)
                .with(USER_VERIFIED_KEY);
    }
}