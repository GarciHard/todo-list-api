package com.garcihard.todolist.controller;

import com.garcihard.todolist.model.dto.LoginRequestDTO;
import com.garcihard.todolist.model.dto.LoginResponseDTO;
import com.garcihard.todolist.model.dto.UserRequestDto;
import com.garcihard.todolist.service.UserService;
import com.garcihard.todolist.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(UserController.BASE_URL)
@RestController
public class UserController {

    static final String BASE_URL = "/api/v1/auth";
    static final String REGISTER_USR_URL = "/register";
    static final String LOGIN_USR_URL = "/login";
    static final String HEALTH_CHECK = "/health";

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping(UserController.REGISTER_USR_URL)
    public ResponseEntity<Void> registerNewUser(@RequestBody @Valid UserRequestDto requestDto){
        userService.createUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(UserController.LOGIN_USR_URL)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO requestDto) {
        LoginResponseDTO response = authenticationService.authenticate(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(UserController.HEALTH_CHECK)
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.status(HttpStatus.OK).body("Ok");
    }
}