package com.sns.common.aop;

import com.sns.common.annotation.LogExecutionTime;
import com.sns.common.component.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // 게시물 조회 API 응답 시간 로깅
    @Around("@annotation(com.sns.common.annotation.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long end = System.currentTimeMillis();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        LogExecutionTime annotation = methodSignature.getMethod().getAnnotation(LogExecutionTime.class);

        HttpMethod httpMethod = annotation.method();
        String description = annotation.desc();

        log.info("[{}] {} 실행 시간: {} ms", httpMethod, description, (end - start));

        return proceed;
    }
}
