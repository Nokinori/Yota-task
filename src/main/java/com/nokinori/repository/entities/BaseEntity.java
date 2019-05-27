package com.nokinori.repository.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Mapped super class for all entities.
 */
@MappedSuperclass
@Data
public abstract class BaseEntity {

    /**
     * The primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Creation time of entity.
     */
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Modification time of entity.
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
