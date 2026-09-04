package com.demo.tcservicebase.common.exception;

import lombok.Getter;

/**
 * 특정 도메인(팀센터/ERP)에 속하지 않는 일반적인 비즈니스 에러.
 * 잘못된 요청, 인증 실패 등.
 */
@Getter
public class CommonBusinessException extends RuntimeException {

    private final CommonErrorCode errorCode;

    public CommonBusinessException(CommonErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}