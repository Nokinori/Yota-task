package com.nokinori.services.exceptions;

/**
 * Exception for problems connected to sim-card activation process.
 */
public class SimCardActivationException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    SimCardActivationException(String message) {
        super(message);
    }
}
