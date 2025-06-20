package com.garcihard.todolist.mapper;

import com.garcihard.todolist.model.dto.UserRequestDto;
import com.garcihard.todolist.model.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User toEntity(UserRequestDto dto);

}
