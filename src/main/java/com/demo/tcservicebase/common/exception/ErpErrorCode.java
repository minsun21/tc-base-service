package com.demo.tcservicebase.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ErpErrorCode implements ErrorCode {

    // rawErpCode가 null인 것은 특정 원본 코드에 매핑되지 않는 기본값 용도
    ERP_INTERFACE_ERROR("ERP_500", "ERP 연동 중 오류가 발생했습니다", HttpStatus.BAD_GATEWAY, null),
    ERP_COMMUNICATION_ERROR("ERP_503", "ERP 서버와 통신할 수 없습니다", HttpStatus.SERVICE_UNAVAILABLE, null),

    // 협의되어 실제 원본 코드가 확정된 것들
    ERP_ITEM_NOT_FOUND("ERP_404", "ERP에 존재하지 않는 품목입니다", HttpStatus.NOT_FOUND, "E0031");

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
    private final String rawErpCode;   // ERP가 실제로 내려주는 원본 코드. 매핑용

    private static final Map<String, ErpErrorCode> RAW_CODE_MAP = Arrays.stream(values())
            .filter(code -> code.rawErpCode != null)
            .collect(Collectors.toMap(code -> code.rawErpCode, Function.identity()));

    public static ErpErrorCode resolve(String rawCode) {
        return RAW_CODE_MAP.getOrDefault(rawCode, ERP_INTERFACE_ERROR);
    }
}
