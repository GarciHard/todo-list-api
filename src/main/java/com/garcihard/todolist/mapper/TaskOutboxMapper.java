package com.garcihard.todolist.mapper;

import com.garcihard.todolist.model.entity.Task;
import com.garcihard.todolist.model.entity.TaskOutbox;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TaskOutboxMapper {

    @Mapping(source = "id", target = "taskId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "title", target = "title")
    @Mapping(target = "id", ignore = true)
    TaskOutbox fromTaskToOutbox(Task task);
}
