package com.nokinori.services.timer;

import com.nokinori.aop.logging.TraceLog;
import com.nokinori.repository.api.GigabytesPackRepo;
import com.nokinori.repository.api.MinutesPackRepo;
import com.nokinori.repository.entities.GigabytesPack;
import com.nokinori.repository.entities.MinutesPack;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Task for that finds and deletes all expired packs.
 */
@Slf4j
@Component
public class ExpiredPackTimerJob implements Job {

    /**
     * Minutes repository.
     */
    private final MinutesPackRepo minutesPackRepo;

    /**
     * Gigabytes repository.
     */
    private final GigabytesPackRepo gigabytesPackRepo;

    public ExpiredPackTimerJob(MinutesPackRepo minutesPackRepo, GigabytesPackRepo gigabytesPackRepo) {
        this.minutesPackRepo = minutesPackRepo;
        this.gigabytesPackRepo = gigabytesPackRepo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TraceLog
    @Transactional
    public void execute(JobExecutionContext context) {
        deleteMinutesPacks();
        deleteGigabytesPacks();
    }

    /**
     * Finds and deletes all expired minutes pack.
     */
    private void deleteMinutesPacks() {
        Iterable<MinutesPack> expiredPacks = minutesPackRepo.findAllWithExpiredAtIsAfter(LocalDateTime.now());
        expiredPacks.forEach(pack -> {
            minutesPackRepo.delete(pack);
            log.debug("Minutes pack with id: {} was deleted", pack.getId());
        });
    }

    /**
     * Finds and deletes all expired gigabytes pack.
     */
    private void deleteGigabytesPacks() {
        Iterable<GigabytesPack> expiredPacks = gigabytesPackRepo.findAllWithExpiredAtIsAfter(LocalDateTime.now());
        expiredPacks.forEach(pack -> {
            gigabytesPackRepo.delete(pack);
            log.debug("Gigabytes pack with id: {} was deleted", pack.getId());
        });
    }
}
