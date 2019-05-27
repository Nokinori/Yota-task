package com.nokinori.aop.validation;

import com.nokinori.repository.api.SimCardRepo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.nokinori.services.exceptions.ExceptionGenerator.throwNotFoundException;
import static com.nokinori.services.exceptions.ExceptionGenerator.throwSimCardBlockageException;

/**
 * Class for sim-card validation.
 */
@Slf4j
@Aspect
@Component
public class SimCardValidator {

    private final SimCardRepo repo;

    public SimCardValidator(SimCardRepo repo) {
        this.repo = repo;
    }

    /**
     * Pointcut where validation must be executed.
     */
    @Pointcut("@annotation(com.nokinori.aop.annotations.RequireActivatedSimCard)")
    void validateSimCard() {
        //empty
    }

    /**
     * Validate sim-card. It must be active.
     * First argument of parameter must be an id of sim-card.
     *
     * @param point the point to wrap.
     * @return proceeded point.
     */
    @Around("validateSimCard()")
    public Object logAround(ProceedingJoinPoint point) throws Throwable {
        Long id = Long.parseLong(String.valueOf(point.getArgs()[0]));
        log.debug("Validate sim-card with id: {}", id);
        boolean isActive = repo.findById(id)
                .orElseThrow(throwNotFoundException(id))
                .isActive();

        if (isActive) {
            return point.proceed();
        } else {
            log.error("Trying to do an operation where allowed only activated sim-cards!");
            throwSimCardBlockageException(id, " is blocked! Must be activated for this operation.");
            // never be returned
            return null;
        }
    }
}
