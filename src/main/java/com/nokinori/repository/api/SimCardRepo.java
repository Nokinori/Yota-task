package com.nokinori.repository.api;

import com.nokinori.repository.entities.SimCards;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SimCardRepo extends BaseRepo<SimCards, Long> {

    Optional<SimCards> findByUserId(Long id);
}
