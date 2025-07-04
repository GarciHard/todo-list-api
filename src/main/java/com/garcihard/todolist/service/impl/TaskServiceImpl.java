package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.event.dto.TaskCreatedEventDTO;
import com.garcihard.todolist.exception.user.ForbiddenResourceForLoggedUserException;
import com.garcihard.todolist.mapper.TaskMapper;
import com.garcihard.todolist.model.dto.TaskRequestDTO;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.dto.TaskUpdateDTO;
import com.garcihard.todolist.model.entity.Task;
import com.garcihard.todolist.model.entity.User;
import com.garcihard.todolist.repository.TaskRepository;
import com.garcihard.todolist.security.CustomUserDetails;
import com.garcihard.todolist.security.util.JwtUtil;
import com.garcihard.todolist.service.TaskService;
import com.garcihard.todolist.util.ApiConstants;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final JwtUtil jwtUtil;
    private final TaskRepository taskRepository;
    private final TaskMapper mapper;
    private final EntityManager entityManager;
    private final ApplicationEventPublisher eventPublisher;

    /*
    * List for tasks of logged user.
    * @param token Authentication token to get the username.
    * */
    @Transactional(readOnly = true)
    @Override
    public List<TaskResponseDTO> listUserTasks(CustomUserDetails userDetails) {
        UUID userId = userDetails.getUserId();

        return taskRepository.findAllByUserId(userId)
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    /*
    * Creates a new task for the logged user.
    * @param token Authentication token to get the username.
    * @param task The request containing the task to be created.
    * @return Task with a unique ID and his creation date.
    * */
    @Transactional
    @Override
    public TaskResponseDTO createUserTask(CustomUserDetails userDetails, TaskRequestDTO task) {
        UUID userId = userDetails.getUserId();

        Task newTask = mapper.toEntity(task);
        User userReference = entityManager.getReference(User.class, userId);
        newTask.setUser(userReference);

        Task createdEntity = taskRepository.save(newTask);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                eventPublisher.publishEvent(TaskCreatedEventDTO.from(createdEntity));
            }
        });

        return mapper.toResponseDto(createdEntity);
    }

    /*
    * Get a specific task.
    * @param token Authentication token to get the username.
    * @param taskId Used to search the desired task.
    * @throws ForbiddenResourceForLoggedUserException If the username mismatch with the task owner.
    * */
    @Transactional(readOnly = true)
    @Override
    public TaskResponseDTO getUserTaskById(CustomUserDetails userDetails, UUID taskId) {
        UUID userId = userDetails.getUserId();
        return mapper.toResponseDto(getTaskEntityByTaskIdAndUserId(taskId, userId));
    }

    /*
    * Delete a specific task.
    * @param token Authentication token to get the username.
    * @param taskId Used to search the task to be deleted.
    * @throws ForbiddenResourceForLoggedUserException If the username mismatch with the task owner.
    * */
    @Transactional
    @Override
    public void deleteUserTaskById(CustomUserDetails userDetails, UUID taskId) {
        UUID userId = userDetails.getUserId();

        int deletedRows = taskRepository.deleteByTaskIdAndUserId(taskId, userId);
        if (deletedRows == 0) {
            throw forbiddenResourceForUserException();
        }
    }

    /*
    * Update a specific task.
    * @param token Authentication token to get the username.
    * @param taskId Used to search the task to be updated.
    * @param updatedTask The request with the task data to be updated.
    * @throws ForbiddenResourceForLoggedUserException If the username mismatch with the task owner.
    * */
    @Transactional
    @Override
    public TaskResponseDTO updateUserTaskById(CustomUserDetails userDetails, UUID taskId, TaskUpdateDTO updatedTask) {
        UUID userId = userDetails.getUserId();

        Task storedTask = getTaskEntityByTaskIdAndUserId(taskId, userId);
        storedTask.setTitle(updatedTask.title());
        storedTask.setDescription(updatedTask.description());
        storedTask.setCompleted(updatedTask.completed());

        storedTask = taskRepository.save(storedTask);
        return mapper.toResponseDto(storedTask);
    }

    /*
    * Fetch the user id from the token.
    * @param token Authentication token to be decoded.
    *
    * @return UUID with the user id from the database.
    * */
    @Deprecated
    private UUID getUserIdFromRequestToken(String token) {
        return jwtUtil.extractUserId(token);
    }

    /*
    * Fetch Task for a specific taskId and userId.
    * @throws ForbiddenResourceForLoggedUserException If the username mismatch with the task owner.
    * */
    private Task getTaskEntityByTaskIdAndUserId(UUID taskId, UUID userId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(this::forbiddenResourceForUserException);
    }

    private ForbiddenResourceForLoggedUserException forbiddenResourceForUserException() {
        return new ForbiddenResourceForLoggedUserException(
                ApiConstants.USER_FORBIDDEN_CODE, ApiConstants.USER_FORBIDDEN_RESOURCE);
    }
}
