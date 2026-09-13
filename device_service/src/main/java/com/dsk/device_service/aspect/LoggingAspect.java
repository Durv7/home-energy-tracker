package com.dsk.device_service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.dsk.device_service.service.*.*(..))")
    public void serviceMethods(){}

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint){
        log.info("Called Service Method: {} with arguments: {}" , joinPoint.getSignature().getName() , joinPoint.getArgs());
    }

    @AfterReturning(value = "serviceMethod()" , returning = "result")
    public void logAfter(JoinPoint joinPoint , Object result){
        log.info("Called Method: {} returned : {}" , joinPoint.getSignature().getName() , result);
    }
}
