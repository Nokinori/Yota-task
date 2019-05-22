package com.nokinori.repository.api;

import com.nokinori.repository.entities.Gigabytes;

import java.util.Optional;

public interface GigabytesRepo extends BaseRepo<Gigabytes, Long> {

    Optional<Gigabytes> findByUserId(Long id);
}
