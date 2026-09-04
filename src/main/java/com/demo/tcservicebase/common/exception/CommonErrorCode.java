package com.demo.tcservicebase.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_REQUEST("COMMON_400", "잘못된 요청입니다", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("COMMON_401", "인증이 필요합니다", HttpStatus.UNAUTHORIZED),
    INTERNAL_ERROR("COMMON_500", "서버 내부 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
