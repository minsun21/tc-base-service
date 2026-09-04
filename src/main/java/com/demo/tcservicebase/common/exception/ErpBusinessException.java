package com.demo.tcservicebase.common.exception;

import lombok.Getter;

/**
 * ERP가 비즈니스적으로 처리를 거부했을 때 던지는 예외.
 * errorCode: 매핑을 거친 우리 쪽 코드 (매핑 없으면 기본값 ERP_INTERFACE_ERROR)
 * erpCode/erpMessage: ERP가 내려준 원본 그대로 (항상 보존)
 */
@Getter
public class ErpBusinessException extends RuntimeException {

    private final ErpErrorCode errorCode;
    private final String erpCode;
    private final String erpMessage;

    public ErpBusinessException(ErpErrorCode errorCode, String erpCode, String erpMessage) {
        super(String.format("ERP 처리 실패 [%s] %s", erpCode, erpMessage));
        this.errorCode = errorCode;
        this.erpCode = erpCode;
        this.erpMessage = erpMessage;
    }
}
