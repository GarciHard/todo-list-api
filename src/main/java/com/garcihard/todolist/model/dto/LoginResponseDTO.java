package com.garcihard.todolist.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseDTO(
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("expires_at")
        Long expiresIn,
        @JsonProperty("access_token")
        String accessToken) {

    public LoginResponseDTO(Long expiresIn, String accessToken) {
        this("Bearer", expiresIn, accessToken);
    }

    public static LoginResponseDTO of(Long expiresIn, String accessToken) {
        return new LoginResponseDTO(expiresIn, accessToken);
    }
}
