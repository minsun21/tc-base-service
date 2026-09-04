package com.demo.tcservicebase.common.exception;

import lombok.Getter;

/**
 * 팀센터 쪽 처리 중 발생하는 비즈니스 에러.
 * 로그인 실패, SOA 호출 실패 등.
 */
@Getter
public class TcBusinessException extends RuntimeException {

    private final TcErrorCode errorCode;

    public TcBusinessException(TcErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
