package com.nokinori.configuration;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.nokinori.repository.api")
@EnableTransactionManagement
@Data
public class Config {

    Integer minutesExpirationTime = 30;

    Integer gigabytesExpirationTime = 30;
}


