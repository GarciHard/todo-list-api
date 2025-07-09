package com.garcihard.todolist.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
        Instant timestamp
) {
    private TaskNotificationDTO (String eventType, UUID taskId, UUID userId, String taskTitle) {
        this(eventType, taskId, userId, taskTitle, Instant.now());
    }

    public static TaskNotificationDTO of(String eventType, UUID taskId, UUID userId, String taskTitle) {
        return new TaskNotificationDTO(eventType, taskId, userId, taskTitle);
    }
}
