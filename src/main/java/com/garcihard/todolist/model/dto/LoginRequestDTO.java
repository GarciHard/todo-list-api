package com.garcihard.todolist.model.dto;

import com.garcihard.todolist.util.UserConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @NotBlank(message = UserConstants.USERNAME_DTO_VALIDATION_NOT_EMPTY_MSG)
        @Size(min = 6, max = 20, message = UserConstants.USERNAME_DTO_VALIDATION_SIZE_MSG)
        String username,

        @Pattern(regexp = UserConstants.PASSWORD_PATTERN, message = UserConstants.PASSWORD_DTO_VALIDATION_MISMATCH_PATTERN_MSG)
        @NotBlank(message = UserConstants.PASSWORD_DTO_VALIDATION_NOT_EMPTY_MSG)
        @Size(min = 8, max = 10, message = UserConstants.PASSWORD_DTO_VALIDATION_SIZE_MSG)
        String password) {
}