package com.demo.tcservicebase.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * 메소드 내부에서 진행상황을 남기고 싶을 때 사용.
 * InterfaceLoggingAspect가 MDC에 심어둔 인터페이스 이름을 자동으로 꺼내서 붙여줌.
 */
@Slf4j
public class InterfaceLog {

    public static void step(String message) {
        String name = MDC.get("interfaceName");
        log.info("  ├─ [{}] {}", name, message);
    }
}
