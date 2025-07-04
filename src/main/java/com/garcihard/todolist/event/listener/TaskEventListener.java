package com.garcihard.todolist.event.listener;

import com.garcihard.todolist.event.TaskEventTypeEnum;
import com.garcihard.todolist.event.dto.TaskCreatedEventDTO;
import com.garcihard.todolist.event.dto.TaskNotificationDTO;
import com.garcihard.todolist.service.RabbitMQMessageProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskEventListener {

    private final RabbitMQMessageProducerService messageProducer;

    @EventListener
    public void handleTaskCreatedEvent(TaskCreatedEventDTO event) {
        log.info("TaskCreatedEvent received for task: {}. Preparing notification", event.eventId());

        TaskNotificationDTO notificationDTO = TaskNotificationDTO.of(
                TaskEventTypeEnum.TASK_CREATED.toString(),
                event.eventId(),
                event.userId(),
                event.title(),
                event.description()
        );

        messageProducer.sendMessage(notificationDTO);
    }
}
