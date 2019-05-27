package com.nokinori.services.impl;

import com.nokinori.aop.annotations.TraceLog;
import com.nokinori.api.io.SimCardRs;
import com.nokinori.mappers.GenericMapper;
import com.nokinori.repository.api.SimCardRepo;
import com.nokinori.repository.entities.SimCard;
import com.nokinori.services.api.SimCardService;
import com.nokinori.services.exceptions.ExceptionGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nokinori.services.exceptions.ExceptionGenerator.throwNotFoundException;

/**
 * Service with operations for sim-card.
 */
@Service
public class SimCardServiceImpl implements SimCardService<SimCardRs> {

    /**
     * Sim-card repository.
     */
    private final SimCardRepo repo;

    /**
     * Mapper for response types.
     */
    private final GenericMapper mapper;

    public SimCardServiceImpl(SimCardRepo repo, GenericMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    /**
     * Activate sim-card.
     *
     * @param id of sim-card.
     */
    @Override
    @TraceLog
    @Transactional
    public void activate(Long id) {
        SimCard simCard = findById(id);
        if (!simCard.isActive())
            simCard.setActive(true);
        else
            ExceptionGenerator.throwSimCardActivationException(id);
    }

    /**
     * Block sim-card.
     *
     * @param id of sim-card.
     */
    @Override
    @TraceLog
    @Transactional
    public void block(Long id) {
        SimCard simCard = findById(id);
        if (simCard.isActive())
            simCard.setActive(false);
        else
            ExceptionGenerator.throwSimCardBlockageException(id);
    }

    /**
     * Create new sim-card.
     *
     * @return Response with id of created sim-card.
     */
    @Override
    @TraceLog
    @Transactional
    public SimCardRs createSimCard() {
        SimCard simCard = new SimCard();
        simCard.setActive(true);
        repo.save(simCard);
        return mapper.toSimCardRs(simCard);
    }

    private SimCard findById(Long id) {
        return repo.findById(id)
                .orElseThrow(throwNotFoundException(id));
    }
}
