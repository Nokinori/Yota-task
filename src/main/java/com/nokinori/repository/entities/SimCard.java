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

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class SimCard extends BaseEntity {

    @Column
    private boolean isActive;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "simCard")
    private List<MinutesPack> minutesPacks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "simCard")
    private List<GigabytesPack> gigabytesPacks = new ArrayList<>();

}
