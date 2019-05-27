package com.nokinori.repository.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The entity mapped for 'Gigabytes_pack' table.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class GigabytesPack extends Pack {

    /**
     * Many to one relation with {@link SimCard} entity
     */
    @ManyToOne
    @JoinColumn(name = "SIM_CARD_ID", nullable = false)
    private SimCard simCard;
}
