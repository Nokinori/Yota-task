package com.nokinori.services.utils;

import com.nokinori.repository.entities.Pack;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Helper class for subtract operation.
 *
 * @param <E> child of Pack entity.
 */
@Component
public class Subtractor<E extends Pack> {

    /**
     * Subtract from pack with defined amount.
     * If amount more than remaining amount of one record. The record will be deleted.
     * <p>
     * If amount more then remaining amount of one record but less then remaining amount of second record.
     * The first record will be deleted, and rest amount will be subtract from second.
     * <p>
     * If amount less then remaining amount of one record. The amount will be subtracted from record.
     *
     * @param packs  of entity.
     * @param amount to be subtracted.
     */
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
