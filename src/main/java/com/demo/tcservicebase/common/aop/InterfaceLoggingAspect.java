package com.demo.tcservicebase.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class InterfaceLoggingAspect {

    private static final String MDC_KEY = "interfaceName";

    @Around("@annotation(loggableInterface)")
    public Object logAround(ProceedingJoinPoint joinPoint, LoggableInterface loggableInterface) throws Throwable {
        String name = loggableInterface.name();
        long start = System.currentTimeMillis();

        String previous = MDC.get(MDC_KEY);
        MDC.put(MDC_KEY, name);
        try {
            log.info("▶ [{}] 시작", name);
            Object result = joinPoint.proceed();
            log.info("└ [{}] 성공 | {}ms", name, System.currentTimeMillis() - start);
            return result;
        } catch (Exception e) {
            // 에러 메시지 상세는 GlobalExceptionHandler가 남기므로, 여기서는 소요시간만 기록함
            log.warn("└ [{}] 실패 | {}ms", name, System.currentTimeMillis() - start);
            throw e;
        } finally {
            if (previous != null) {
                MDC.put(MDC_KEY, previous);
            } else {
                MDC.remove(MDC_KEY);
            }
        }
    }
}
