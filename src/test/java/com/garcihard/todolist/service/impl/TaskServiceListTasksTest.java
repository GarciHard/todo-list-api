package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.mapper.TaskMapper;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.entity.Task;
import com.garcihard.todolist.repository.TaskRepository;
import com.garcihard.todolist.security.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.garcihard.todolist.util.DefaultTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceListTasksTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper mapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private UUID userId;
    private Task task;
    private TaskResponseDTO taskResponse;

    @BeforeEach
    void setupTestData() {
        userId = USER_ID;
        task = getDefaultTask(userId);
        taskResponse = getDefaultTaskResponseDto(userId);
    }

    @Test
    void listUserTasks_validToken_returnsTaskList() {
        when(jwtUtil.extractUserId(VALID_TOKEN)).thenReturn(userId);
        when(taskRepository.findAllByUserId(userId)).thenReturn(List.of(task));
        when(mapper.toResponseDto(task)).thenReturn(taskResponse);

        List<TaskResponseDTO> result = taskService.listUserTasks(VALID_TOKEN);

        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(taskResponse);
        verify(jwtUtil, times(1)).extractUserId(VALID_TOKEN);
        verify(taskRepository, times(1)).findAllByUserId(userId);
        verify(mapper, times(1)).toResponseDto(task);
    }

    @Test
    void listUserTasks_validToken_returnsEmptyList() {
        when(jwtUtil.extractUserId(VALID_TOKEN)).thenReturn(userId);
        when(taskRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        List<TaskResponseDTO> result = taskService.listUserTasks(VALID_TOKEN);

        assertThat(result).isEmpty();
        verify(jwtUtil, times(1)).extractUserId(VALID_TOKEN);
        verify(taskRepository, times(1)).findAllByUserId(userId);
        verify(mapper, times(0)).toResponseDto(task);
    }

    @Test
    void listUserTasks_invalidToken_throwsJwtException() {
        String invalidToken = "invalid-jwt-token";
        when(jwtUtil.extractUserId(invalidToken)).thenThrow(new JwtException("Invalid or expired JWT token."));

        assertThatThrownBy(() -> taskService.listUserTasks(invalidToken))
                .isInstanceOf(JwtException.class)
                .hasMessage("Invalid or expired JWT token.");

        verify(jwtUtil, times(1)).extractUserId(invalidToken);
        verify(taskRepository, times(0)).findAllByUserId(any());
        verify(mapper, times(0)).toResponseDto(any());
    }
}
