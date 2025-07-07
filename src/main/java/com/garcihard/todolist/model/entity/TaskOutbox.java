package com.garcihard.todolist.model.entity;

import com.garcihard.todolist.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "tasks_outbox")
public class TaskOutbox extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID taskId;
    private UUID userId;
    private String eventType;
    private String title;
    private boolean dispatched;
}