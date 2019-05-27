package com.nokinori.services.api;

/**
 * Interface for sim-card services.
 */
public interface SimCardService<T> {

    /**
     * Activate sim-card.
     *
     * @param id of sim-card.
     */
    void activate(Long id);

    /**
     * Block sim-card.
     *
     * @param id of sim-card.
     */
    void block(Long id);

    /**
     * Create new sim-card.
     */
    T createSimCard();
}
