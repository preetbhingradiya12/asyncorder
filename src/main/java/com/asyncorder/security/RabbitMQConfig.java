package com.asyncorder.security;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "order-exchange";
    public static final String ORDER_QUEUE = "order-queue";
    public static final String ORDER_ROUTING_KEY = "order.created";

    // Email Events
    public static final String EMAIL_QUEUE = "email-queue";
    public static final String EMAIL_ROUTING_KEY = "email.outbox";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(ORDER_QUEUE);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ORDER_ROUTING_KEY);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue()).to(exchange()).with(EMAIL_ROUTING_KEY);
    }
}
