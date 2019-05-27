package com.nokinori.repository.api;

import com.nokinori.repository.entities.GigabytesPack;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Repository interface for {@link GigabytesPack}.
 */
@Repository
public interface GigabytesPackRepo extends BaseRepo<GigabytesPack, Long> {

    /**
     * Find all packs where expiredAt time is after param.
     *
     * @param time to compare with expiredAt.
     * @return {@link Iterable} of {@link GigabytesPack}
     */
    @Query("select g from GigabytesPack g where g.expiresAt <= :time")
    Iterable<GigabytesPack> findAllWithExpiredAtIsAfter(@Param("time") LocalDateTime time);
}
