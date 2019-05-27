package com.nokinori.configuration;

import com.nokinori.services.timer.ExpiredPackTimerJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for timer job.
 *
 * @see ExpiredPackTimerJob
 */
@Configuration
public class JobConfiguration {

    /**
     * Properties holder.
     */
    private final Properties properties;

    public JobConfiguration(Properties properties) {
        this.properties = properties;
    }

    /**
     * Produce the JobDetail instance defined for {@link ExpiredPackTimerJob}.
     *
     * @return the defined JobDetail.
     */
    @Bean
    public JobDetail jobDetails() {
        return JobBuilder.newJob(ExpiredPackTimerJob.class)
                .withIdentity("Expired_Packs_Job")
                .withDescription("Finds and delete all expired packs")
                .storeDurably()
                .build();
    }

    /**
     * Produce the Trigger instance defined for {@link ExpiredPackTimerJob}.
     *
     * @param jobDetail the defined JobDetail.
     * @return the defined Trigger.
     */
    @Bean
    public Trigger jobTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("Expired_Packs_Job_Trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(properties.getCronSchedulerForExpiredPacksJob()))
                .build();
    }
}
