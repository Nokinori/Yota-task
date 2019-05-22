package com.nokinori.repository.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Gigabytes extends BaseEntity {

    @Column
    private Long amount;

    @Column
    private LocalDateTime expiresAt;
}
