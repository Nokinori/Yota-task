package com.nokinori.services.impl;

import com.nokinori.aop.logging.TraceLog;
import com.nokinori.api.io.SimCardRs;
import com.nokinori.configuration.Properties;
import com.nokinori.mappers.GenericMapper;
import com.nokinori.repository.api.SimCardRepo;
import com.nokinori.repository.entities.GigabytesPack;
import com.nokinori.repository.entities.Pack;
import com.nokinori.repository.entities.SimCard;
import com.nokinori.services.Subtractor;
import com.nokinori.services.api.BillingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static com.nokinori.services.exceptions.ExceptionGenerator.throwNotFoundException;
import static com.nokinori.services.exceptions.ExceptionGenerator.throwPackNotFoundException;

/**
 * Service with operations for gigabytes packs.
 */
@Service("gigabytesService")
public class GigabytesServiceImpl implements BillingService<SimCardRs> {

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
    private final Subtractor<GigabytesPack> subtractor;

    public GigabytesServiceImpl(SimCardRepo repo, Properties properties, GenericMapper mapper, Subtractor<GigabytesPack> subtractor) {
        this.repo = repo;
        this.properties = properties;
        this.mapper = mapper;
        this.subtractor = subtractor;
    }

    /**
     * Get gigabytes packs for sim-card with id.
     *
     * @param id of entity.
     * @return interface object with response values.
     */
    @Override
    @TraceLog
    @Transactional
    public SimCardRs get(Long id) {
        List<GigabytesPack> gigabytes = findById(id)
                .getGigabytesPacks();

        return SimCardRs.builder()
                .simCardId(id)
                .gigabytesPacks(mapper.toGigabytePacksRs(gigabytes))
                .build();
    }

    /**
     * Add new pack of gigabytes with defined amount.
     *
     * @param id     of entity.
     * @param amount to be added.
     */
    @Override
    @TraceLog
    @Transactional
    public void add(Long id, Integer amount) {
        SimCard simCard = findById(id);

        simCard.getGigabytesPacks()
                .add(createGigabytes(simCard, amount));
        repo.save(simCard);
    }

    /**
     * Subtract from pack of gigabytes with defined amount.
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
    @Transactional
    public void subtract(Long id, Integer amount) {
        SimCard simCard = findById(id);

        if (simCard.getGigabytesPacks()
                .isEmpty()) {
            throwPackNotFoundException(id);
        }

        subtract(simCard, amount);
    }

    private void subtract(SimCard simCard, Integer amount) {
        List<GigabytesPack> gigabytesPacks = simCard.getGigabytesPacks();
        gigabytesPacks.sort(Comparator.comparing(Pack::getExpiresAt));
        subtractor.subtract(gigabytesPacks, amount);
        repo.save(simCard);
    }


    private GigabytesPack createGigabytes(SimCard simCard, Integer amount) {
        GigabytesPack gigabytesPack = new GigabytesPack();
        gigabytesPack.setSimCard(simCard);
        gigabytesPack.setAmount(amount);
        gigabytesPack.setExpiresAt(LocalDateTime.now()
                .plusDays(properties.getExpirationTimeForGigabytesPack()));
        return gigabytesPack;
    }

    private SimCard findById(Long id) {
        return repo.findById(id)
                .orElseThrow(throwNotFoundException(id));
    }
}
