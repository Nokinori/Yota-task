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

@Service("gigabytesService")
public class GigabytesServiceImpl implements BillingService<SimCardRs> {

    private final SimCardRepo repo;

    private final Properties properties;

    private final GenericMapper mapper;

    private final Subtractor<GigabytesPack> subtractor;

    public GigabytesServiceImpl(SimCardRepo repo, Properties properties, GenericMapper mapper, Subtractor<GigabytesPack> subtractor) {
        this.repo = repo;
        this.properties = properties;
        this.mapper = mapper;
        this.subtractor = subtractor;
    }

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

    @Override
    @TraceLog
    @Transactional
    public void add(Long id, Integer amount) {
        SimCard simCard = findById(id);

        simCard.getGigabytesPacks()
                .add(createGigabytes(simCard, amount));
        repo.save(simCard);
    }

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
