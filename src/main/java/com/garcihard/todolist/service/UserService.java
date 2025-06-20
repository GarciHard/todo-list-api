package com.garcihard.todolist.service;

import com.garcihard.todolist.model.dto.UserRequestDto;

public interface UserService {
    void createUser(UserRequestDto requestDto);

//    public UserDetails loadUserByUsername(String username);

}
