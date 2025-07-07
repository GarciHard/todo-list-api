package com.garcihard.todolist.service;

import com.garcihard.todolist.model.entity.Task;

public interface TaskOutboxService {

    void createTaskOutbox(Task task);
    void updateNotification(Task task);
}
