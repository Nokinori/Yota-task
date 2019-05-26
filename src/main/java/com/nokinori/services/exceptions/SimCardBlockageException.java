package com.nokinori.services.exceptions;

/**
 * Exception for problems connected to sim-card blockage process.
 */
public class SimCardBlockageException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    SimCardBlockageException(String message) {
        super(message);
    }
}
