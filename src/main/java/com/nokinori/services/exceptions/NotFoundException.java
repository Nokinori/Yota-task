package com.nokinori.services.exceptions;

/**
 * Exception for not found entity in DB.
 */
public class NotFoundException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    NotFoundException(String message) {
        super(message);
    }
}
