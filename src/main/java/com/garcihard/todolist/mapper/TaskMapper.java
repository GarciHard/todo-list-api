package com.garcihard.todolist.mapper;

import com.garcihard.todolist.model.dto.TaskRequestDTO;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.entity.Task;
import org.mapstruct.Mapper;

@Mapper
public interface TaskMapper {


    TaskResponseDTO toResponseDto(Task entity);
    Task toEntity(TaskRequestDTO dto);
}
