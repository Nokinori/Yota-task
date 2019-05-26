package com.nokinori.services.timer;

import com.nokinori.configuration.Properties;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;

/**
 * Timer scheduler that executes task.
 */
@Component
public class ExpiredPackProcessorTimerService {

    /**
     * Properties holder
     */
    private final Properties properties;

    /**
     * Task Scheduler for task execution
     */
    private final TaskScheduler scheduler;

    /**
     * Task to be executed
     */
    private final ExpiredPackTimerTask task;

    public ExpiredPackProcessorTimerService(TaskScheduler scheduler, Properties properties, ExpiredPackTimerTask task) {
        this.scheduler = scheduler;
        this.properties = properties;
        this.task = task;
    }

    /**
     * Starts the timer service.
     */
    @PostConstruct
    public void doTask() {
        scheduler.scheduleWithFixedDelay(task,
                Duration.ofMinutes(properties.getDelayOfTimerServiceTaskExecutor()));
    }
}
