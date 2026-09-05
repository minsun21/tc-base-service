package com.demo.tcservicebase.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class TcErrorCodeTest {

    @Test
    void ITEM_NOT_FOUND는_404와_TC_404를_가진다() {
        assertThat(TcErrorCode.ITEM_NOT_FOUND.getCode()).isEqualTo("TC_404");
        assertThat(TcErrorCode.ITEM_NOT_FOUND.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void LOGIN_FAILED는_401과_TC_401을_가진다() {
        assertThat(TcErrorCode.LOGIN_FAILED.getCode()).isEqualTo("TC_401");
        assertThat(TcErrorCode.LOGIN_FAILED.getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void SOA_CALL_FAILED는_503과_TC_503을_가진다() {
        assertThat(TcErrorCode.SOA_CALL_FAILED.getCode()).isEqualTo("TC_503");
        assertThat(TcErrorCode.SOA_CALL_FAILED.getHttpStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
