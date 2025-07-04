package com.garcihard.todolist.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.cfg.defs.UUIDDef;

import java.time.Instant;
import java.util.UUID;

public record TaskNotificationDTO(
        @JsonProperty("event_type")
        String eventType,
        @JsonProperty("task_id")
        UUID taskId,
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("task_title")
        String taskTitle,
        @JsonProperty("task_description")
        String taskDescription,
        Instant timestamp
) {
    private TaskNotificationDTO (String eventType, UUID taskId, UUID userId, String taskTitle, String taskDescription) {
        this(eventType, taskId, userId, taskTitle, taskDescription, Instant.now());
    }

    public static TaskNotificationDTO of(String eventType, UUID taskId, UUID userId, String taskTitle, String taskDescription) {
        return new TaskNotificationDTO(eventType, taskId, userId, taskTitle, taskDescription);
    }
}
