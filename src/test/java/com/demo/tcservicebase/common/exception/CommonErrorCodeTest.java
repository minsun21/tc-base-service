package com.demo.tcservicebase.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class CommonErrorCodeTest {

    @Test
    void INVALID_REQUEST는_400과_COMMON_400을_가진다() {
        assertThat(CommonErrorCode.INVALID_REQUEST.getCode()).isEqualTo("COMMON_400");
        assertThat(CommonErrorCode.INVALID_REQUEST.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void UNAUTHORIZED는_401과_COMMON_401을_가진다() {
        assertThat(CommonErrorCode.UNAUTHORIZED.getCode()).isEqualTo("COMMON_401");
        assertThat(CommonErrorCode.UNAUTHORIZED.getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void INTERNAL_ERROR는_500과_COMMON_500을_가진다() {
        assertThat(CommonErrorCode.INTERNAL_ERROR.getCode()).isEqualTo("COMMON_500");
        assertThat(CommonErrorCode.INTERNAL_ERROR.getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
