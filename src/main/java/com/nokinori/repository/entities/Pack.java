package com.nokinori.repository.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public class Pack extends BaseEntity {

    @Column
    private Integer amount;

    @Column
    private LocalDateTime expiresAt;
}
