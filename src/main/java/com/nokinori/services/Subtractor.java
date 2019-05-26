package com.nokinori.services;

import com.nokinori.repository.entities.Pack;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Subtractor<E extends Pack> {

    public void subtract(List<E> packs, Integer amount) {
        CopyOnWriteArrayList<E> copyOfMinutes = new CopyOnWriteArrayList<>(packs);
        for (E pack : copyOfMinutes) {
            if (amount == 0 || amount < pack.getAmount()) {
                pack.setAmount(pack.getAmount() - amount);
                break;
            } else {
                amount -= pack.getAmount();
                copyOfMinutes.remove(pack);
                packs.remove(pack);
            }
        }
    }
}
