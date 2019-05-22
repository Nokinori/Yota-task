package com.nokinori.aop.logging;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.nokinori.aop.logging.TraceLog)")
    void loggable() {
        //empty
    }

    @Around("loggable()")
    @SneakyThrows
    public Object logAround(ProceedingJoinPoint point) {
        log.trace("Start to execute {} with params {}", point.getSignature(), point.getArgs());
        Object proceed = point.proceed();
        log.trace("End to execute {} with params {}", point.getSignature(), point.getArgs());
        return proceed;
    }

}
