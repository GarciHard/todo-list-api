package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.exception.user.UsernameAlreadyExistException;
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
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /*
    * Creates a new user.
    *
    * @param requestDto The request containing username and password.
    * @throws UsernameAlreadyExistException If the username already exist.
    * */
    @Transactional
    @Override
    public void createUser(UserRequestDto requestDto) {
        String username = requestDto.username();

        if(userRepository.existsByUsername(username)) {
            throw buildUsernameExistException(username);
        }

        User entity = userMapper.toEntity(requestDto);
        entity.setPassword(passwordEncoder.encode(requestDto.password()));

        try { // If 2+ users tried to create the same username at the same time.
            userRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw buildUsernameExistException(username);
        }
    }

    private UsernameAlreadyExistException buildUsernameExistException(String username) {
        return new UsernameAlreadyExistException(ApiConstants.USER_CONFLICT_CODE,
                ApiConstants.USER_ALREADY_TAKEN.formatted(username));
    }
}