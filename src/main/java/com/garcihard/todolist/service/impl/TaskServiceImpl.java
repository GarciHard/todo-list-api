package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.exception.user.UserException;
import com.garcihard.todolist.mapper.TaskMapper;
import com.garcihard.todolist.model.dto.TaskRequestDTO;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.dto.TaskUpdateDTO;
import com.garcihard.todolist.model.entity.Task;
import com.garcihard.todolist.model.entity.User;
import com.garcihard.todolist.repository.TaskRepository;
import com.garcihard.todolist.repository.UserRepository;
import com.garcihard.todolist.security.util.JwtUtil;
import com.garcihard.todolist.service.TaskService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final JwtUtil jwtUtil;
    private final TaskRepository taskRepository;
    private final TaskMapper mapper;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Transactional
    @Override
    public List<TaskResponseDTO> listAllUserTask(String token) {
        UUID userId = getUserIdFromRequestToken(token);

        return taskRepository.findAllByUserId(userId)
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Transactional
    @Override
    public TaskResponseDTO createUserTask(String token, TaskRequestDTO task) {
        UUID userId = getUserIdFromRequestToken(token);

        Task newTask = mapper.toEntity(task);
        User userReference = entityManager.getReference(User.class, userId);
        newTask.setUser(userReference);

        Task createdEntity = taskRepository.save(newTask);
        log.info("Created Task: {}", createdEntity.toString());

        return mapper.toResponseDto(createdEntity);
    }

    @Transactional
    @Override
    public TaskResponseDTO getUserTaskById(String token, UUID taskId) {
        UUID userId = getUserIdFromRequestToken(token);
        return mapper.toResponseDto(getTaskEntityByTaskIdAndUserId(taskId, userId));
    }

    @Transactional
    @Override
    public void deleteUserTaskById(String token, UUID taskId) {
        UUID userId = getUserIdFromRequestToken(token);

        int deletedRows = taskRepository.deletedUserTaskById(taskId, userId);
        if (deletedRows == 0) {
            throw resourceNotFoundForLoggedUser();
        }
    }

    @Transactional
    @Override
    public TaskResponseDTO updateUserTaskById(String token, UUID taskId, TaskUpdateDTO updatedTask) {
        UUID userId = getUserIdFromRequestToken(token);

        Task storedTask = getTaskEntityByTaskIdAndUserId(taskId, userId);
        storedTask.setTitle(updatedTask.title());
        storedTask.setDescription(updatedTask.description());
        storedTask.setCompleted(updatedTask.completed());

        storedTask = taskRepository.save(storedTask);
        return mapper.toResponseDto(storedTask);
    }

    @Transactional(readOnly = true)
    protected UUID getUserIdFromRequestToken(String token) {
        token = jwtUtil.extractJwtFromRequest(token);
        String username = jwtUtil.extractUsername(token);
        return userRepository.getIdByUsername(username);
    }

    private Task getTaskEntityByTaskIdAndUserId(UUID taskId, UUID userId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(this::resourceNotFoundForLoggedUser);
    }

    private UserException resourceNotFoundForLoggedUser() {
        return new UserException("NOT_FOUND", "Task Not Found.");
    }

}
