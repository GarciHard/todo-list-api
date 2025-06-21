package com.garcihard.todolist.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garcihard.todolist.exception.dto.ErrorResponse;
import com.garcihard.todolist.util.ApiConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        String message = "Invalid or expired JWT token.";

        log.warn("Unauthorized Error: {}", authException.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(ApiConstants.CODE_UNAUTHORIZED, message, null);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        mapper.writeValue(response.getWriter(), errorResponse);
    }
}
