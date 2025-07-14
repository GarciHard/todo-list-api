package com.garcihard.todolist.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spring.rabbitmq.template")
public record RabbitMQProperties(
        @NotBlank(message = "Queue should not be empty - null.")
        String defaultReceiveQueue,
        @NotBlank(message = "Exchange should not be empty - null.")
        String exchange,
        @NotBlank(message = "Routing Key should not be empty - null.")
        String routingKey
) {
}
