package com.nokinori.services.impl;

import com.nokinori.aop.logging.TraceLog;
import com.nokinori.api.io.SimCardRs;
import com.nokinori.configuration.Properties;
import com.nokinori.mappers.GenericMapper;
import com.nokinori.repository.api.SimCardRepo;
import com.nokinori.repository.entities.MinutesPack;
import com.nokinori.repository.entities.Pack;
import com.nokinori.repository.entities.SimCard;
import com.nokinori.services.Subtractor;
import com.nokinori.services.api.BillingService;
import com.nokinori.services.exceptions.ExceptionGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static com.nokinori.services.exceptions.ExceptionGenerator.throwNotFoundException;

@Service("minutesService")
public class MinutesServiceImpl implements BillingService<SimCardRs> {

    private final SimCardRepo repo;

    private final Properties properties;

    private final GenericMapper mapper;

    private final Subtractor<MinutesPack> subtractor;

    public MinutesServiceImpl(SimCardRepo repo, Properties properties, GenericMapper mapper, Subtractor<MinutesPack> subtractor) {
        this.repo = repo;
        this.properties = properties;
        this.mapper = mapper;
        this.subtractor = subtractor;
    }

    @Override
    @TraceLog
    @Transactional
    public SimCardRs get(Long id) {
        List<MinutesPack> minutes = findById(id)
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
        SimCard simCard = findById(id);

        simCard.getMinutesPacks()
                .add(createMinutesPack(simCard, amount));
        repo.save(simCard);
    }

    @Override
    @TraceLog
    @Transactional
    public void subtract(Long id, Integer amount) {
        SimCard simCard = findById(id);

        if (simCard.getMinutesPacks()
                .isEmpty()) {
            ExceptionGenerator.throwPackNotFoundException(id);
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
                .plusDays(properties.getExpirationTimeForMinutesPack()));
        return minutesPack;
    }

    private SimCard findById(Long id) {
        return repo.findById(id)
                .orElseThrow(throwNotFoundException(id));
    }
}
