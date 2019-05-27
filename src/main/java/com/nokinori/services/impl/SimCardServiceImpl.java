package com.nokinori.services.impl;

import com.nokinori.aop.annotations.TraceLog;
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
public class SimCardServiceImpl implements SimCardService {

    /**
     * Sim-card repository.
     */
    private final SimCardRepo repo;

    public SimCardServiceImpl(SimCardRepo repo) {
        this.repo = repo;
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

    private SimCard findById(Long id) {
        return repo.findById(id)
                .orElseThrow(throwNotFoundException(id));
    }
}
