package com.nokinori.services.api;

public interface BillingService<T> {

    T get(Long id);

    void add(Long id, Integer amount);

    void subtract(Long id, Integer amount);

}
