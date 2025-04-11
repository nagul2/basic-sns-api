package com.sns.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // 게시물 조회 API 응답 시간 로깅
    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long end = System.currentTimeMillis();

        log.info("API: {} 실행 시간: {} ms", joinPoint.getSignature(), (end - start));
        return proceed;
    }
}
