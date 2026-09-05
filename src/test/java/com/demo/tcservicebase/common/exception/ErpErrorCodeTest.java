package com.demo.tcservicebase.common.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErpErrorCodeTest {

    @Test
    void 매핑된_원본코드는_해당_ErpErrorCode로_변환된다() {
        assertThat(ErpErrorCode.resolve("E0031")).isEqualTo(ErpErrorCode.ERP_ITEM_NOT_FOUND);
    }

    @Test
    void 매핑되지_않은_원본코드는_기본값으로_변환된다() {
        assertThat(ErpErrorCode.resolve("E9999")).isEqualTo(ErpErrorCode.ERP_INTERFACE_ERROR);
    }

    @Test
    void 원본코드가_null이어도_기본값으로_변환된다() {
        assertThat(ErpErrorCode.resolve(null)).isEqualTo(ErpErrorCode.ERP_INTERFACE_ERROR);
    }
}
