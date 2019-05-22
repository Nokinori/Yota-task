package com.nokinori.api.handlers;

public enum ErrorCode {

    NOT_FOUND(1001),

    ACTIVATION_EXCEPTION(1002);

    private final Integer value;

    ErrorCode(Integer value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
