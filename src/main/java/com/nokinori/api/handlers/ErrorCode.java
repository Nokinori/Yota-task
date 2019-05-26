package com.nokinori.api.handlers;

/**
 * Enum with custom error codes for responses.
 */
public enum ErrorCode {

    NOT_FOUND(1001),

    ACTIVATION_EXCEPTION(1002),

    BLOCKAGE_EXCEPTION(1003),

    VALIDATION_EXCEPTION(1004);

    private final Integer value;

    ErrorCode(Integer value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
