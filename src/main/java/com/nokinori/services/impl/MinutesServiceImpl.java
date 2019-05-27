package com.nokinori.services.impl;

import com.nokinori.aop.annotations.RequireActivatedSimCard;
import com.nokinori.aop.annotations.TraceLog;
import com.nokinori.api.io.SimCardRs;
import com.nokinori.configuration.Properties;
import com.nokinori.mappers.GenericMapper;
import com.nokinori.repository.api.SimCardRepo;
import com.nokinori.repository.entities.MinutesPack;
import com.nokinori.repository.entities.Pack;
import com.nokinori.repository.entities.SimCard;
import com.nokinori.services.api.BillingService;
import com.nokinori.services.exceptions.ExceptionGenerator;
import com.nokinori.services.utils.Subtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static com.nokinori.services.exceptions.ExceptionGenerator.throwNotFoundException;

/**
 * Service with operations for minutes packs.
 */
@Service("minutesService")
public class MinutesServiceImpl implements BillingService<SimCardRs> {

    /**
     * Sim-card repository.
     */
    private final SimCardRepo repo;

    /**
     * Properties holder.
     */
    private final Properties properties;

    /**
     * Mapper for response types.
     */
    private final GenericMapper mapper;

    /**
     * Subtraction class.
     */
    private final Subtractor<MinutesPack> subtractor;

    public MinutesServiceImpl(SimCardRepo repo, Properties properties, GenericMapper mapper, Subtractor<MinutesPack> subtractor) {
        this.repo = repo;
        this.properties = properties;
        this.mapper = mapper;
        this.subtractor = subtractor;
    }

    /**
     * Get minutes packs for sim-card with id.
     *
     * @param id of entity.
     * @return interface object with response values.
     */
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

    /**
     * Add new pack of minutes with defined amount.
     *
     * @param id     of entity.
     * @param amount to be added.
     */
    @Override
    @TraceLog
    @RequireActivatedSimCard
    @Transactional
    public void add(Long id, Integer amount) {
        SimCard simCard = findById(id);

        simCard.getMinutesPacks()
                .add(createMinutesPack(simCard, amount));
        repo.save(simCard);
    }

    /**
     * Subtract from pack of minutes with defined amount.
     * If amount more than remaining amount of one record connected to sim-card. The record will be deleted.
     * <p>
     * If amount more then remaining amount of one record but less then remaining amount of second record.
     * The first record will be deleted, and rest amount will be subtract from second.
     * <p>
     * If amount less then remaining amount of one record. The amount will be subtracted from record.
     *
     * @param id     of entity.
     * @param amount to be subtracted.
     */
    @Override
    @TraceLog
    @RequireActivatedSimCard
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
