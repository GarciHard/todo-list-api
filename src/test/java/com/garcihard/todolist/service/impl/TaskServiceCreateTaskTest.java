package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.mapper.TaskMapper;
import com.garcihard.todolist.model.dto.TaskRequestDTO;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.entity.Task;
import com.garcihard.todolist.model.entity.User;
import com.garcihard.todolist.repository.TaskRepository;
import com.garcihard.todolist.security.CustomUserDetails;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.garcihard.todolist.util.DefaultTestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceCreateTaskTest {

    @Mock
    EntityManager entityManager;

    @Mock
    TaskRepository taskRepository;

    @Mock
    TaskMapper mapper;

    @InjectMocks
    TaskServiceImpl taskService;

    private UUID userId;
    private TaskRequestDTO taskRequest;
    private Task task;
    private User user;
    private TaskResponseDTO taskResponse;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setupTestData() {
        userId = USER_ID;
        taskRequest = getDefaultTaskRequestDto(userId);
        task = getDefaultTask(userId);
        user = getDefaultUserReference(userId);
        taskResponse = getDefaultTaskResponseDto(userId);
        userDetails = getDefaultUserDetails();
    }

    @Test
    void shouldCreateTaskAndReturnDtoWhenTokenAndTaskRequestAreValid(){
        when(mapper.toEntity(taskRequest)).thenReturn(task);
        when(entityManager.getReference(User.class, userId)).thenReturn(user);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(mapper.toResponseDto(task)).thenReturn(taskResponse);

        TaskResponseDTO result = taskService.createUserTask(userDetails, taskRequest);

        assertThat(result.id()).isEqualTo(taskResponse.id());
        assertThat(result.title()).isEqualTo(taskResponse.title());
        assertThat(result.description()).isEqualTo(taskResponse.description());
        assertThat(result.completed()).isEqualTo(taskResponse.completed());
        assertThat(result.createdAt()).isEqualTo(taskResponse.createdAt());
        assertThat(result.updatedAt()).isEqualTo(taskResponse.updatedAt());

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(1)).save(taskCaptor.capture());
        Task savedTask = taskCaptor.getValue();
        assertThat(savedTask.getUser()).isEqualTo(user);
        assertThat(savedTask.getTitle()).isEqualTo(TASK_TITLE);

        verify(mapper, times(1)).toEntity(taskRequest);
        verify(entityManager, times(1)).getReference(User.class, userId);
        verify(mapper, times(1)).toResponseDto(task);

    }
}