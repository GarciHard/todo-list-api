package com.garcihard.todolist.event;

import com.garcihard.todolist.event.dto.TaskNotificationDTO;
import com.garcihard.todolist.model.entity.TaskOutbox;
import com.garcihard.todolist.repository.TaskOutboxRepository;
import com.garcihard.todolist.service.RabbitMQMessageProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskMessageProcessor {

    private final RabbitMQMessageProducerService messageProducer;
    private final TaskOutboxRepository outboxRepository;

    @Transactional
    public void processSingleMessage(TaskOutbox message) {
        try {
            TaskNotificationDTO notificationMessage = createNotificationDTO(message);
            messageProducer.sendMessage(notificationMessage);

            message.setDispatched(true);
            outboxRepository.save(message);
        } catch (Exception e) {
            log.error("Failed to process outbox message ID: {}. Will retry later.", message.getId());
            throw new RuntimeException("Failed to process message, forcing rollback for message: " + message.getId(), e);
        }

    }

    private TaskNotificationDTO createNotificationDTO(TaskOutbox outbox) {
        return TaskNotificationDTO.of(
                outbox.getEventType(),
                outbox.getTaskId(),
                outbox.getUserId(),
                outbox.getTitle()
        );
    }
}
