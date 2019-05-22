package com.nokinori.services.impl;

import com.nokinori.aop.logging.TraceLog;
import com.nokinori.repository.api.SimCardRepo;
import com.nokinori.repository.entities.SimCards;
import com.nokinori.services.api.SimCardService;
import com.nokinori.services.exceptions.NotFoundException;
import com.nokinori.services.exceptions.SimCardActivationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SimCardServiceImpl implements SimCardService {

    private final SimCardRepo simCardRepo;

    @Autowired
    public SimCardServiceImpl(SimCardRepo simCardRepo) {
        this.simCardRepo = simCardRepo;
    }

    @Override
    @TraceLog
    @Transactional
    public void activate(Long id) {
        SimCards simCards = findSimCardByUserId(id);
        if (!simCards.isActive())
            simCards.setActive(true);
        else
            throw new SimCardActivationException("Sim-card already activated");
    }

    @Override
    @TraceLog
    @Transactional
    public void block(Long id) {
        SimCards simCards = findSimCardByUserId(id);
        if (simCards.isActive())
            simCards.setActive(false);
        else
            throw new SimCardActivationException("Sim-card already blocked");
    }

    private SimCards findSimCardByUserId(Long id) {
        return simCardRepo.findByUserId(id)
                .orElseThrow(NotFoundException::new);
    }
}
