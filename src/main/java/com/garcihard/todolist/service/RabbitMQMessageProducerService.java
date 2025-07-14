package com.garcihard.todolist.service;

import com.garcihard.todolist.config.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RabbitMQMessageProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties properties;

    @Async
    public void sendMessage(Object notification) {
        try {
            log.info("Attempting to send a message to exchange: {}", properties.exchange());
            rabbitTemplate.convertAndSend(
                    properties.exchange(),
                    properties.routingKey(),
                    notification
            );
            log.info("Message sent successfully");
        } catch (AmqpException ex) {
            log.error("Failed to send message to RabbitMQ. Payload: {}, Exchange: {}, Reason: {}", notification, properties.exchange(), ex.getMessage());
        }
    }
}
