package com.garcihard.todolist.model.dto;

import com.garcihard.todolist.util.ApiConstants;
import jakarta.validation.constraints.NotBlank;


public record TaskUpdateDTO(
        @NotBlank(message = ApiConstants.TASK_TITLE_DTO_VALIDATION_NOT_EMPTY)
        String title,
        String description,
        boolean completed) {
}
