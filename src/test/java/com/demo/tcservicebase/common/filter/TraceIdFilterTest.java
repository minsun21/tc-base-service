package com.demo.tcservicebase.common.filter;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TraceIdFilterTest {

    private final TraceIdFilter filter = new TraceIdFilter();

    @Test
    void 요청_처리중에는_traceId가_MDC에_존재하고_처리후에는_제거된다() throws Exception {
        AtomicReference<String> capturedTraceId = new AtomicReference<>();

        filter.doFilter(
                new MockHttpServletRequest(),
                new MockHttpServletResponse(),
                (req, res) -> capturedTraceId.set(MDC.get("traceId")));

        assertThat(capturedTraceId.get()).isNotBlank().hasSize(8);
        assertThat(MDC.get("traceId")).isNull();
    }

    @Test
    void 체인_처리중_예외가_발생해도_traceId는_제거된다() {
        assertThatThrownBy(() -> filter.doFilter(
                new MockHttpServletRequest(),
                new MockHttpServletResponse(),
                (req, res) -> {
                    throw new IllegalStateException("체인 실패");
                }))
                .isInstanceOf(IllegalStateException.class);

        assertThat(MDC.get("traceId")).isNull();
    }
}
