package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.exception.custom.ForbiddenResourceForLoggedUserException;
import com.garcihard.todolist.mapper.TaskMapper;
import com.garcihard.todolist.model.dto.TaskRequestDTO;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.dto.TaskUpdateDTO;
import com.garcihard.todolist.model.entity.Task;
import com.garcihard.todolist.repository.TaskRepository;
import com.garcihard.todolist.service.TaskOutboxService;
import com.garcihard.todolist.service.TaskService;
import com.garcihard.todolist.util.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper mapper;

    private final TaskOutboxService outboxService;

    /*
    * List for tasks of logged user.
    * @param userId Retrieved from header.
    * */
    @Transactional(readOnly = true)
    @Override
    public List<TaskResponseDTO> listUserTasks(UUID userId) {
        return taskRepository.findAllByUserId(userId)
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    /*
    * Creates a new task for the logged user.
    * @param userId Retrieved from header.
    * @param task The request containing the task to be created.
    * @return Task with a unique ID and his creation date.
    * */
    @Transactional
    @Override
    public TaskResponseDTO createUserTask(UUID userId, TaskRequestDTO task) {
        Task newTask = mapper.toEntity(task);
        newTask.setUserId(userId);

        Task createdEntity = taskRepository.save(newTask);
        outboxService.createTaskOutbox(createdEntity);

        return mapper.toResponseDto(createdEntity);
    }

    /*
    * Get a specific task.
    * @param userId Retrieved from header.
    * @param taskId Used to search the desired task.
    * @throws ForbiddenResourceForLoggedUserException If the username mismatch with the task owner.
    * */
    @Transactional(readOnly = true)
    @Override
    public TaskResponseDTO getUserTaskById(UUID userId, UUID taskId) {
        return mapper.toResponseDto(getTaskEntityByTaskIdAndUserId(taskId, userId));
    }

    /*
    * Delete a specific task.
    * @param userId Retrieved from header.
    * @param taskId Used to search the task to be deleted.
    * @throws ForbiddenResourceForLoggedUserException If the username mismatch with the task owner.
    * */
    @Transactional
    @Override
    public void deleteUserTaskById(UUID userId, UUID taskId) {
        int deletedRows = taskRepository.deleteByTaskIdAndUserId(taskId, userId);
        if (deletedRows == 0) {
            throw forbiddenResourceForUserException();
        }
    }

    /*
    * Update a specific task.
    * @param userId Retrieved from header.
    * @param taskId Used to search the task to be updated.
    * @param updatedTask The request with the task data to be updated.
    * @throws ForbiddenResourceForLoggedUserException If the username mismatch with the task owner.
    * */
    @Transactional
    @Override
    public TaskResponseDTO updateUserTaskById(UUID userId, UUID taskId, TaskUpdateDTO updatedTask) {
        Task storedTask = getTaskEntityByTaskIdAndUserId(taskId, userId);
        storedTask.setTitle(updatedTask.title());
        storedTask.setDescription(updatedTask.description());
        storedTask.setCompleted(updatedTask.completed());

        storedTask = taskRepository.save(storedTask);
        outboxService.updateNotification(storedTask);

        return mapper.toResponseDto(storedTask);
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
