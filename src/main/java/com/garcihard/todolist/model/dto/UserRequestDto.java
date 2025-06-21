package com.garcihard.todolist.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.garcihard.todolist.util.ApiConstants;

public record UserRequestDto(
        @NotBlank(message = ApiConstants.USERNAME_DTO_VALIDATION_NOT_EMPTY_MSG)
        @Size(min = 6, max = 20, message = ApiConstants.USERNAME_DTO_VALIDATION_SIZE_MSG)
        String username,

        @Pattern(regexp = ApiConstants.PASSWORD_PATTERN, message = ApiConstants.PASSWORD_DTO_VALIDATION_MISMATCH_PATTERN_MSG)
        @NotBlank(message = ApiConstants.PASSWORD_DTO_VALIDATION_NOT_EMPTY_MSG)
        @Size(min = 8, max = 10, message = ApiConstants.PASSWORD_DTO_VALIDATION_SIZE_MSG)
        String password) {
}
