package com.nokinori.services.api;

public interface BillingService<T> {

    T get(Long id);

    T add(Long id, Long amount);

    T subtract(Long id, Long amount);

}
