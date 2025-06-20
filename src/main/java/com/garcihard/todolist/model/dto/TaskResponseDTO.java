package com.garcihard.todolist.model.dto;

import java.time.Instant;

public record TaskResponseDTO(
        String id,
        String title,
        String description,
        boolean completed,
        Instant createdAt,
        Instant updatedAt
) {
}
