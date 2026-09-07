package com.dsk.user_service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class ExecutionAspect {

    @Pointcut("execution(* com.dsk.user_service.controller.*.*(..))")
    public void controllerMethods(){}

    @Around("controllerMethods()")
    public Object trackExecution(ProceedingJoinPoint pjp) throws Throwable{
        long start = System.nanoTime();
        try{
            return pjp.proceed();
        }finally {
            long end = System.nanoTime();
            long elapsedNanoSec = end - start;
            long elapsedMiliSec = TimeUnit.NANOSECONDS.toMillis(elapsedNanoSec);
            String signature = pjp.getSignature().getName();

            log.info("Controller Method {} executed in {} ms" , signature , elapsedMiliSec);
        }
    }
}
