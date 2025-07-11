package com.garcihard.todolist.repository;

import com.garcihard.todolist.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findAllByUserId(UUID userId);
    Optional<Task> findByIdAndUserId(UUID taskId, UUID userId);

    @Modifying
    @Query("DELETE FROM Task t where t.id = :taskId AND t.userId = :userId")
    int deleteByTaskIdAndUserId(@Param("taskId")UUID taskId, @Param("userId") UUID userId);
}
