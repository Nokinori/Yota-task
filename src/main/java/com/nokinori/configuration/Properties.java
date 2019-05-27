package com.nokinori.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Class holder for properties with prefix = "billing.service".
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "billing.service")
public class Properties {

    /**
     * Holder for "expiration-time-for-minutes-pack".
     * Default value is 30 min.
     */
    private Integer expirationTimeForMinutesPack = 30;

    /**
     * Holder for "expiration-time-for-gigabytes-pack".
     * Default value is 30 min.
     */
    private Integer expirationTimeForGigabytesPack = 30;

    /**
     * Cron expression for job with expired packs.
     */
    private String cronSchedulerForExpiredPacksJob = "0 * * ? * * *";
}
