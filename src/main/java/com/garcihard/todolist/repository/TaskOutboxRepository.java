package com.garcihard.todolist.repository;

import com.garcihard.todolist.model.entity.TaskOutbox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskOutboxRepository extends JpaRepository<TaskOutbox, UUID> {

    /**
     * Encuentra todos los mensajes que no han sido despachados (dispatched = false),
     * ordenados por fecha de creación para asegurar un procesamiento FIFO (First-In, First-Out).
     *
     * Spring Data JPA construye la consulta automáticamente a partir del nombre del método.
     *
     * @param dispatched El estado del despacho a buscar (siempre 'false' en nuestro caso).
     * @param pageable El objeto de paginación para limitar los resultados al tamaño del lote (BATCH_SIZE).
     * @return Una lista de mensajes de outbox listos para ser procesados.
     */
    List<TaskOutbox> findByDispatchedOrderByCreatedAtAsc(boolean dispatched, Pageable pageable);

}
