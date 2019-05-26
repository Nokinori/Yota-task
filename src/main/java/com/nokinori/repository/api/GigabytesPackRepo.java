package com.nokinori.repository.api;

import com.nokinori.repository.entities.GigabytesPack;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface GigabytesPackRepo extends BaseRepo<GigabytesPack, Long> {

    @Query("select g from GigabytesPack g where g.expiresAt <= :currentTime")
    Iterable<GigabytesPack> findAllWithExpiredAtIsAfter(@Param("currentTime") LocalDateTime currentTime);
}
