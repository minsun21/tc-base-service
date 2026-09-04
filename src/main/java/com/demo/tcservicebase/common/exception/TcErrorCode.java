package com.demo.tcservicebase.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TcErrorCode implements ErrorCode {

    ITEM_NOT_FOUND("TC_404", "팀센터에서 해당 품목을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    LOGIN_FAILED("TC_401", "팀센터 로그인에 실패했습니다", HttpStatus.UNAUTHORIZED),
    SOA_CALL_FAILED("TC_503", "팀센터 SOA 서비스 호출에 실패했습니다", HttpStatus.SERVICE_UNAVAILABLE);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
