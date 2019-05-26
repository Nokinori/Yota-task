package com.nokinori.repository.api;

import com.nokinori.repository.entities.MinutesPack;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MinutesPackRepo extends BaseRepo<MinutesPack, Long> {

    @Query("select m from MinutesPack m where m.expiresAt <= :currentTime")
    Iterable<MinutesPack> findAllWithExpiredAtIsAfter(@Param("currentTime") LocalDateTime currentTime);
}
