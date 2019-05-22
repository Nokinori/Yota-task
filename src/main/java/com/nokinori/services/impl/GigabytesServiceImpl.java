package com.nokinori.services.impl;

import com.nokinori.aop.logging.TraceLog;
import com.nokinori.api.io.GigabytesRs;
import com.nokinori.configuration.Config;
import com.nokinori.mappers.GenericMapper;
import com.nokinori.repository.api.GigabytesRepo;
import com.nokinori.repository.entities.Gigabytes;
import com.nokinori.services.api.BillingService;
import com.nokinori.services.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service("gigabyteService")
public class GigabytesServiceImpl implements BillingService<GigabytesRs> {

    private final GigabytesRepo gigabytesRepo;

    private final Config config;

    private final GenericMapper mapper;

    public GigabytesServiceImpl(GigabytesRepo gigabytesRepo, Config config, GenericMapper mapper) {
        this.gigabytesRepo = gigabytesRepo;
        this.config = config;
        this.mapper = mapper;
    }


    @Override
    @TraceLog
    @Transactional
    public GigabytesRs get(Long id) {
        Gigabytes gigabytes = gigabytesRepo.findByUserId(id)
                .orElseThrow(NotFoundException::new);
        return mapper.toGigabyteRs(gigabytes);
    }

    @Override
    @TraceLog
    @Transactional
    public GigabytesRs add(Long id, Long amount) {
        Optional<Gigabytes> optionalMinutes = gigabytesRepo.findByUserId(id);
        Gigabytes gigabytes = optionalMinutes.map(m -> plus(m, amount))
                .orElseGet(() -> saveNew(id, amount));
        return mapper.toGigabyteRs(gigabytes);
    }

    @Override
    @TraceLog
    @Transactional
    public GigabytesRs subtract(Long id, Long amount) {
        Optional<Gigabytes> optionalMinutes = gigabytesRepo.findByUserId(id);
        Gigabytes gigabytes = optionalMinutes.map(m -> subtract(m, amount))
                .orElseThrow(NotFoundException::new);
        return mapper.toGigabyteRs(gigabytes);
    }

    private Gigabytes plus(Gigabytes gigabytes, Long amount) {
        gigabytes.setAmount(gigabytes.getAmount() + amount);
        return gigabytes;
    }

    private Gigabytes subtract(Gigabytes gigabytes, Long amount) {
        gigabytes.setAmount(gigabytes.getAmount() - amount);
        return gigabytes;
    }

    private Gigabytes saveNew(Long id, Long amount) {
        Gigabytes gigabytes = new Gigabytes();
        gigabytes.setUserId(id);
        gigabytes.setAmount(amount);
        gigabytes.setExpiresAt(LocalDateTime.now()
                .plusMinutes(config.getMinutesExpirationTime()));
        gigabytesRepo.save(gigabytes);
        return gigabytes;
    }
}
