package com.garcihard.todolist.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitMQProperties.class)
@Configuration
public class RabbitMQConfig {

    private final RabbitMQProperties properties;

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(properties.defaultReceiveQueue())
                .withArgument("x-dead-letter-exchange", "dlx-exchange")
                .withArgument("x-dead-letter-routing-key", "dlx.routing.key")
                .build();
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(properties.exchange());
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(properties.routingKey());
    }

    @Bean
    public Queue dlq() {
        return new Queue("dlq-queue", true);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange("dlx-exchange");
    }

    @Bean
    public Binding dlqBinding(Queue dlq, DirectExchange dlxExchange) {
        return BindingBuilder.bind(dlq).to(dlxExchange).with("dlx.routing.key");
    }
}