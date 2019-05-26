package com.nokinori.services.impl;

import com.nokinori.aop.logging.TraceLog;
import com.nokinori.api.io.SimCardRs;
import com.nokinori.configuration.Config;
import com.nokinori.mappers.GenericMapper;
import com.nokinori.repository.api.SimCardRepo;
import com.nokinori.repository.entities.MinutesPack;
import com.nokinori.repository.entities.Pack;
import com.nokinori.repository.entities.SimCard;
import com.nokinori.services.Subtractor;
import com.nokinori.services.api.BillingService;
import com.nokinori.services.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service("minutesService")
public class MinutesServiceImpl implements BillingService<SimCardRs> {

    private final SimCardRepo repo;

    private final Config config;

    private final GenericMapper mapper;

    private final Subtractor<MinutesPack> subtractor;

    public MinutesServiceImpl(SimCardRepo repo, Config config, GenericMapper mapper, Subtractor<MinutesPack> subtractor) {
        this.repo = repo;
        this.config = config;
        this.mapper = mapper;
        this.subtractor = subtractor;
    }

    @Override
    @TraceLog
    @Transactional
    public SimCardRs get(Long id) {
        List<MinutesPack> minutes = repo.findById(id)
                .orElseThrow(NotFoundException::new)
                .getMinutesPacks();

        return SimCardRs.builder()
                .simCardId(id)
                .minutesPacks(mapper.toMinutesPacksRs(minutes))
                .build();
    }

    @Override
    @TraceLog
    @Transactional
    public void add(Long id, Integer amount) {
        SimCard simCard = repo.findById(id)
                .orElseThrow(NotFoundException::new);

        simCard.getMinutesPacks()
                .add(createMinutesPack(simCard, amount));
        repo.save(simCard);
    }

    @Override
    @TraceLog
    @Transactional
    public void subtract(Long id, Integer amount) {
        SimCard simCard = repo.findById(id)
                .orElseThrow(NotFoundException::new);

        if (simCard.getMinutesPacks()
                .isEmpty()) {
            throw new NotFoundException();
        }

        subtract(simCard, amount);
    }

    private void subtract(SimCard simCard, Integer amount) {
        List<MinutesPack> minutesPacks = simCard.getMinutesPacks();
        minutesPacks.sort(Comparator.comparing(Pack::getExpiresAt));
        subtractor.subtract(minutesPacks, amount);
        repo.save(simCard);
    }


    private MinutesPack createMinutesPack(SimCard simCard, Integer amount) {
        MinutesPack minutesPack = new MinutesPack();
        minutesPack.setSimCard(simCard);
        minutesPack.setAmount(amount);
        minutesPack.setExpiresAt(LocalDateTime.now()
                .plusMinutes(config.getMinutesExpirationTime()));
        return minutesPack;
    }
}
