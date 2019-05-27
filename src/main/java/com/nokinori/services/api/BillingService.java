package com.nokinori.services.api;

/**
 * Interface for billing services.
 *
 * @param <T> type of return value.
 */
public interface BillingService<T> {

    /**
     * Get the record by id.
     *
     * @param id of entity.
     * @return value presented by {@link T}.
     */
    T get(Long id);

    /**
     * Add new record for entity with id.
     *
     * @param id     of entity.
     * @param amount to be added.
     */
    void add(Long id, Integer amount);

    /**
     * Subtract new record for entity with id.
     *
     * @param id     of entity.
     * @param amount to be subtracted.
     */
    void subtract(Long id, Integer amount);

}
