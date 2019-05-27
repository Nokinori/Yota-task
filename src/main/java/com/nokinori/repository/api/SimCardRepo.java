package com.nokinori.repository.api;

import com.nokinori.repository.entities.SimCard;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link SimCard}.
 */
@Repository
public interface SimCardRepo extends BaseRepo<SimCard, Long> {

}
