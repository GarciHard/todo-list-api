package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.event.TaskEventTypeEnum;
import com.garcihard.todolist.mapper.TaskOutboxMapper;
import com.garcihard.todolist.model.entity.Task;
import com.garcihard.todolist.model.entity.TaskOutbox;
import com.garcihard.todolist.repository.TaskOutboxRepository;
import com.garcihard.todolist.service.TaskOutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class TaskOutboxServiceImpl implements TaskOutboxService {

    private final TaskOutboxMapper mapper;
    private final TaskOutboxRepository repository;

    @Transactional
    @Override
    public void createTaskOutbox(Task task) {
        TaskOutbox outbox = mapper.fromTaskToOutbox(task);
        outbox.setEventType(TaskEventTypeEnum.TASK_CREATED.name());
        repository.save(outbox);
    }

    @Transactional
    @Override
    public void updateNotification(Task task) {
        TaskOutbox outbox = mapper.fromTaskToOutbox(task);
        outbox.setEventType(TaskEventTypeEnum.TASK_UPDATED.name());
        repository.save(outbox);
    }
}
