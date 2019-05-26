package com.nokinori.services.impl;

import com.nokinori.aop.logging.TraceLog;
import com.nokinori.repository.api.SimCardRepo;
import com.nokinori.repository.entities.SimCard;
import com.nokinori.services.api.SimCardService;
import com.nokinori.services.exceptions.NotFoundException;
import com.nokinori.services.exceptions.SimCardActivationException;
import com.nokinori.services.exceptions.SimCardBlockageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SimCardServiceImpl implements SimCardService {

    private final SimCardRepo repo;

    @Autowired
    public SimCardServiceImpl(SimCardRepo repo) {
        this.repo = repo;
    }

    @Override
    @TraceLog
    @Transactional
    public void activate(Long id) {
        SimCard simCard = findSimCardById(id);
        if (!simCard.isActive())
            simCard.setActive(true);
        else
            throw new SimCardActivationException("Sim-card already activated");
    }

    @Override
    @TraceLog
    @Transactional
    public void block(Long id) {
        SimCard simCard = findSimCardById(id);
        if (simCard.isActive())
            simCard.setActive(false);
        else
            throw new SimCardBlockageException("Sim-card already blocked");
    }

    private SimCard findSimCardById(Long id) {
        return repo.findById(id)
                .orElseThrow(NotFoundException::new);
    }
}
