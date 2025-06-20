package com.garcihard.todolist.model.dto;

import java.util.UUID;

public record TaskRequestDTO(String title,
                             String description,
                             UUID userId) {
}
