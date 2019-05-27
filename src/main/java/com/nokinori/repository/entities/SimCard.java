package com.nokinori.repository.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * The entity mapped for 'Sim_Card' table.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class SimCard extends BaseEntity {

    /**
     * Is Active sim-card.
     */
    @Column
    private boolean isActive;

    /**
     * One to many relation with {@link MinutesPack} entity
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "simCard")
    private List<MinutesPack> minutesPacks = new ArrayList<>();

    /**
     * One to many relation with {@link GigabytesPack} entity
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "simCard")
    private List<GigabytesPack> gigabytesPacks = new ArrayList<>();

}
