package com.nokinori.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration class.
 */
@Configuration
@EnableConfigurationProperties(Properties.class)
@EnableJpaRepositories(basePackages = "com.nokinori.repository.api")
@EnableTransactionManagement
@EnableScheduling
public class Config {

}


