package com.nokinori.aop.logging;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Trace log aspect.
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * Pointcut where trace log must be executed.
     */
    @Pointcut("@annotation(com.nokinori.aop.logging.TraceLog)")
    void loggable() {
        //empty
    }

    /**
     * Trace log that wraps invokes method.
     *
     * @param point the point to wrap.
     * @return proceeded point.
     */
    @Around("loggable()")
    @SneakyThrows
    public Object logAround(ProceedingJoinPoint point) {
        log.trace("Start to execute {} with params {}", point.getSignature(), point.getArgs());
        Object proceed = point.proceed();
        log.trace("End to execute {} with params {}", point.getSignature(), point.getArgs());
        return proceed;
    }

}
