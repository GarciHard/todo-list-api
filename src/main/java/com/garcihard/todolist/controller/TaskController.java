package com.garcihard.todolist.controller;

import com.garcihard.todolist.model.dto.TaskRequestDTO;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.dto.TaskUpdateDTO;
import com.garcihard.todolist.service.TaskService;
import com.garcihard.todolist.util.UserIdValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping(TaskController.BASE_URL)
@RestController
public class TaskController {

    static final String BASE_URL = "/api/v1/task";
    static final String TASK_BY_ID = "/{id}";
    static final String ID = "id";

    static final String AUTH_HEADER = "X-User-Id";

    private final TaskService taskService;
    private final UserIdValidator userIdValidator;

    @GetMapping()
    public ResponseEntity<List<TaskResponseDTO>> getAllTaskFromUser(
            @RequestHeader(AUTH_HEADER) String headerUserId) {
        UUID userId = userIdValidator.validateUserId(headerUserId);
        List<TaskResponseDTO> response = taskService.listUserTasks(userId);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> addNewUserTask(
            @RequestHeader(AUTH_HEADER) String headerUserId,
            @RequestBody @Valid TaskRequestDTO newTask) {
        UUID userId = userIdValidator.validateUserId(headerUserId);
        TaskResponseDTO response = taskService.createUserTask(userId, newTask);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(TaskController.TASK_BY_ID)
    public ResponseEntity<TaskResponseDTO> getTaskById(
            @RequestHeader(AUTH_HEADER) String headerUserId,
            @PathVariable(ID) UUID taskId) {
        UUID userId = userIdValidator.validateUserId(headerUserId);
        TaskResponseDTO response = taskService.getUserTaskById(userId, taskId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(TaskController.TASK_BY_ID)
    public ResponseEntity<TaskResponseDTO> updateTaskById(
            @RequestHeader(AUTH_HEADER) String headerUserId,
            @PathVariable(ID) UUID taskId,
            @RequestBody @Valid TaskUpdateDTO updatedTask) {
        UUID userId = userIdValidator.validateUserId(headerUserId);
        TaskResponseDTO response = taskService.updateUserTaskById(userId, taskId, updatedTask);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(TaskController.TASK_BY_ID)
    public ResponseEntity<Void> deleteTaskById(
            @RequestHeader(AUTH_HEADER) String headerUserId,
            @PathVariable(ID) UUID taskId) {
        UUID userId = userIdValidator.validateUserId(headerUserId);
        taskService.deleteUserTaskById(userId, taskId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
