package com.nokinori.services.impl;

import com.nokinori.aop.logging.TraceLog;
import com.nokinori.repository.api.SimCardRepo;
import com.nokinori.repository.entities.SimCard;
import com.nokinori.services.api.SimCardService;
import com.nokinori.services.exceptions.ExceptionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nokinori.services.exceptions.ExceptionGenerator.throwNotFoundException;

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
        SimCard simCard = findById(id);
        if (!simCard.isActive())
            simCard.setActive(true);
        else
            ExceptionGenerator.throwSimCardActivationException(id);
    }

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
