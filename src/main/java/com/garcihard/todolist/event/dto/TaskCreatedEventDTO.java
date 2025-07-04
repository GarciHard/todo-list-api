package com.garcihard.todolist.event.dto;

import com.garcihard.todolist.model.entity.Task;

import java.util.UUID;

public record TaskCreatedEventDTO(
        UUID eventId,
        UUID userId,
        String title,
        String description
) {
    public static TaskCreatedEventDTO from(Task task) {
        return new TaskCreatedEventDTO(
                task.getId(),
                task.getUser().getId(),
                task.getTitle(),
                task.getDescription()
        );
    }
}
