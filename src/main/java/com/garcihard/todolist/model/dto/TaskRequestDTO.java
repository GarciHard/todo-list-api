package com.garcihard.todolist.model.dto;

import com.garcihard.todolist.util.ApiConstants;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record TaskRequestDTO(
        @NotBlank(message = ApiConstants.TASK_TITLE_DTO_VALIDATION_NOT_EMPTY)
        String title,
        String description,
        UUID userId) {
}
