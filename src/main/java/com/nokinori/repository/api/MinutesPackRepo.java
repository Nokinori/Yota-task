package com.nokinori.repository.api;

import com.nokinori.repository.entities.MinutesPack;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Repository interface for {@link MinutesPack}.
 */
@Repository
public interface MinutesPackRepo extends BaseRepo<MinutesPack, Long> {

    /**
     * Find all packs where expiredAt time is after param.
     *
     * @param time to compare with expiredAt.
     * @return {@link Iterable} of {@link MinutesPack}
     */
    @Query("select m from MinutesPack m where m.expiresAt <= :time")
    Iterable<MinutesPack> findAllWithExpiredAtIsAfter(@Param("time") LocalDateTime time);
}
