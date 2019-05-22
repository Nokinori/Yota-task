package com.nokinori.services.impl;

import com.nokinori.aop.logging.TraceLog;
import com.nokinori.api.io.MinutesRs;
import com.nokinori.configuration.Config;
import com.nokinori.mappers.GenericMapper;
import com.nokinori.repository.api.MinutesRepo;
import com.nokinori.repository.entities.Minutes;
import com.nokinori.services.api.BillingService;
import com.nokinori.services.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service("minutesService")
public class MinutesServiceImpl implements BillingService<MinutesRs> {

    private final MinutesRepo minutesRepo;

    private final Config config;

    private final GenericMapper mapper;

    public MinutesServiceImpl(MinutesRepo minutesRepo, Config config, GenericMapper mapper) {
        this.minutesRepo = minutesRepo;
        this.config = config;
        this.mapper = mapper;
    }

    @Override
    @TraceLog
    @Transactional
    public MinutesRs get(Long id) {
        Minutes minutes = minutesRepo.findByUserId(id)
                .orElseThrow(NotFoundException::new);
        return mapper.toMinutesRs(minutes);
    }

    @Override
    @TraceLog
    @Transactional
    public MinutesRs add(Long id, Long amount) {
        Optional<Minutes> optionalMinutes = minutesRepo.findByUserId(id);
        Minutes minutes = optionalMinutes.map(m -> plus(m, amount))
                .orElseGet(() -> saveNew(id, amount));
        return mapper.toMinutesRs(minutes);
    }

    @Override
    @TraceLog
    @Transactional
    public MinutesRs subtract(Long id, Long amount) {
        Optional<Minutes> optionalMinutes = minutesRepo.findByUserId(id);
        Minutes minutes = optionalMinutes.map(m -> subtract(m, amount))
                .orElseThrow(NotFoundException::new);
        return mapper.toMinutesRs(minutes);
    }

    private Minutes plus(Minutes minutes, Long amount) {
        minutes.setAmount(minutes.getAmount() + amount);
        return minutes;
    }

    private Minutes subtract(Minutes minutes, Long amount) {
        minutes.setAmount(minutes.getAmount() - amount);
        return minutes;
    }

    private Minutes saveNew(Long id, Long amount) {
        Minutes minutes = new Minutes();
        minutes.setUserId(id);
        minutes.setAmount(amount);
        minutes.setExpiresAt(LocalDateTime.now()
                .plusMinutes(config.getMinutesExpirationTime()));
        minutesRepo.save(minutes);
        return minutes;
    }
}
