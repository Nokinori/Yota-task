package com.nokinori.repository.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Mapped super class for pack entities.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public class Pack extends BaseEntity {

    /**
     * Amount of pack.
     */
    @Column
    private Integer amount;

    /**
     * Time when pack will be expired.
     */
    @Column
    private LocalDateTime expiresAt;
}
