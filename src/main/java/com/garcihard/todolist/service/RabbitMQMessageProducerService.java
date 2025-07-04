package com.garcihard.todolist.service;

import com.garcihard.todolist.config.RabbitMQConfig;
import com.garcihard.todolist.event.dto.TaskNotificationDTO;
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

    @Async
    public void sendMessage(TaskNotificationDTO notificationDTO) {
        try {
            log.info("Attempting to send a message to exchange: {}", RabbitMQConfig.EXCHANGE_NAME);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY,
                    notificationDTO
            );
            log.info("Message sent successfully");
        } catch (AmqpException ex) {
            log.error("Failed to send message to RabbitMQ. Payload: {}, Exchange: {}, Reason: {}", notificationDTO, RabbitMQConfig.EXCHANGE_NAME, ex.getMessage());
        }
    }
}
