package com.garcihard.todolist.event.emmiter;

import com.garcihard.todolist.event.TaskMessageProcessor;
import com.garcihard.todolist.model.entity.TaskOutbox;
import com.garcihard.todolist.repository.TaskOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskEventEmitter {

    private final TaskMessageProcessor messageProcessor;
    private final TaskOutboxRepository outboxRepository;

    @Value("${outbox.processor.batch-size:100}")
    private int BATCH_SIZE;

    @Scheduled(fixedDelay = 1000)
    public void emmitNotifications() {
        Pageable pageable = PageRequest.of(0, BATCH_SIZE);
        List<TaskOutbox> pendingTaskNotifications = outboxRepository.findByDispatchedOrderByCreatedAtAsc(false, pageable);

        if (!pendingTaskNotifications.isEmpty()) {
            log.info("Found {} unsent messages. Processing batch of size {}...", pendingTaskNotifications.size(), BATCH_SIZE);
        }

        for (TaskOutbox taskOutbox: pendingTaskNotifications) {
            messageProcessor.processSingleMessage(taskOutbox);
        }

    }

}
