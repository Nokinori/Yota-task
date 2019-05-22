package com.nokinori.repository.api;

import com.nokinori.repository.entities.Minutes;

import java.util.Optional;

public interface MinutesRepo extends BaseRepo<Minutes, Long> {

    Optional<Minutes> findByUserId(Long aLong);
}
