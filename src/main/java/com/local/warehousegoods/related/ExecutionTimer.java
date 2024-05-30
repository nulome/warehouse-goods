package com.local.warehousegoods.related;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;


@Aspect
@Component
public class ExecutionTimer {

    @Around("@annotation(com.local.warehousegoods.related.TrackingExecutionTimer)")
    public Object trackTime(ProceedingJoinPoint pjp) throws Throwable {
        final MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        final String className = methodSignature.getDeclaringType().getSimpleName();
        final String methodName = methodSignature.getName();

        Instant start = Instant.now();
        try {
            return pjp.proceed();
        } finally {
            Instant end = Instant.now();
            System.out.println("Execution time for " + className + "." + methodName + " :: " +
                    (Duration.between(start, end).toMillis()) + " ms");
        }

//        String str = pjp.getSignature().toString();
//        String[] split = str.split("\\.");
//        String nameMethod = split[split.length - 2] + "." + split[split.length - 1];
//        System.out.println("Execution time for " + nameMethod +  " :: " + (Duration.between(start, end).toMillis()));
    }
}
