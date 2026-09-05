package com.demo.tcservicebase.common.aop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * InterfaceLoggingAspect가 @LoggableInterface 메소드 호출을 가로채서
 * MDC를 올바르게 심고/복원하는지, 예외를 삼키지 않고 그대로 전파하는지 검증.
 * 스프링 컨텍스트 없이 AspectJProxyFactory로 직접 프록시를 만들어 테스트한다.
 */
class InterfaceLoggingAspectTest {

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    @Test
    void 정상_호출시_반환값을_그대로_전달하고_호출후_MDC를_제거한다() {
        Target proxy = createProxy();

        String result = proxy.greet();

        assertThat(result).isEqualTo("hello");
        assertThat(MDC.get("interfaceName")).isNull();
    }

    @Test
    void 호출_전에_MDC값이_있었다면_호출후_원래값으로_복원한다() {
        MDC.put("interfaceName", "outer-context");
        Target proxy = createProxy();

        proxy.greet();

        assertThat(MDC.get("interfaceName")).isEqualTo("outer-context");
    }

    @Test
    void 예외_발생시_MDC를_복원하면서_예외를_그대로_전파한다() {
        Target proxy = createProxy();

        assertThatThrownBy(proxy::explode)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("boom");
        assertThat(MDC.get("interfaceName")).isNull();
    }

    private Target createProxy() {
        AspectJProxyFactory factory = new AspectJProxyFactory(new Target());
        factory.addAspect(new InterfaceLoggingAspect());
        return factory.getProxy();
    }

    static class Target {

        @LoggableInterface(name = "테스트-성공")
        public String greet() {
            return "hello";
        }

        @LoggableInterface(name = "테스트-실패")
        public String explode() {
            throw new IllegalStateException("boom");
        }
    }
}
