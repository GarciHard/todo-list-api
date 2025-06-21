package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.domain.UserDomainService;
import com.garcihard.todolist.exception.user.UserException;
import com.garcihard.todolist.mapper.UserMapper;
import com.garcihard.todolist.model.dto.UserRequestDto;
import com.garcihard.todolist.model.entity.User;
import com.garcihard.todolist.repository.UserRepository;
import com.garcihard.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.garcihard.todolist.util.ApiConstants;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserDomainService userDomainService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserRequestDto requestDto) {
        String username = requestDto.username();

        if (userDomainService.isUsernameTaken(requestDto.username())) {
            throwUsernameTaken(username);
        }

        User entity = userMapper.toEntity(requestDto);
        entity.setPassword(passwordEncoder.encode(requestDto.password()));

        try { // If 2+ users tried to create the same username at the same time.
            userRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throwUsernameTaken(username);
        }
    }

    private void throwUsernameTaken(String username) {
        throw new UserException(ApiConstants.USER_CONFLICT_CODE,
                ApiConstants.USER_ALREADY_TAKEN.formatted(username));
    }
}