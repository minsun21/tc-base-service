package com.demo.tcservicebase.common.dto;

import com.demo.tcservicebase.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


/**
 * [성공 응답 예시]
 * {
 *   "success": true,
 *   "data": { "itemId": "ITEM-0001", "erpDocNo": "DOC-98765" },
 *   "timestamp": "2026-09-04T14:23:11"
 * }
 * [일반 실패 응답 예시]
 * {
 *   "success": false,
 *   "error": {
 *     "code": "TC_404",
 *     "message": "품목을 찾을 수 없습니다"
 *   },
 *   "timestamp": "2026-09-04T14:23:11"
 * }
 * [ERP 실패 응답 예시] - erpCode/erpMessage까지 포함된 경우
 * {
 *   "success": false,
 *   "error": {
 *     "code": "ERP_500",
 *     "message": "ERP 연동 중 오류가 발생했습니다",
 *     "erpCode": "E0031",
 *     "erpMessage": "존재하지 않는 품목입니다"
 *   },
 *   "timestamp": "2026-09-04T14:23:11"
 * }
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorDetail error;
    private final LocalDateTime timestamp;

    // builder를 private으로 감춰서, 외부에서는 아래 정적 팩토리 메소드로만 생성 가능
    @Builder(access = AccessLevel.PRIVATE)
    private ApiResponse(boolean success, T data, ErrorDetail error, LocalDateTime timestamp) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.timestamp = timestamp;
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String message) {
        return fail(errorCode, message, null, null);
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String message, String erpCode, String erpMessage) {
        ErrorDetail error = ErrorDetail.builder()
                .code(errorCode.getCode())
                .message(message)
                .erpCode(erpCode)
                .erpMessage(erpMessage)
                .build();

        return ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Getter
    @Builder
    public static class ErrorDetail {
        private String code;         // 시스템 에러 코드
        private String message;      // 시스템 에러 메시지
        private String erpCode;      // ERP 원본 에러 코드
        private String erpMessage;   // ERP 원본 에러 메시지
    }
}