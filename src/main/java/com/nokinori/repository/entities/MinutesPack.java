package com.nokinori.repository.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class MinutesPack extends Pack {

    @ManyToOne
    @JoinColumn(name = "SIM_CARD_ID", nullable = false)
    private SimCard simCard;
}
