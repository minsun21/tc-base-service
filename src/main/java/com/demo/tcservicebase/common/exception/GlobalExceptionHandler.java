package com.demo.tcservicebase.common.exception;


import com.demo.tcservicebase.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    // Spring MVC 표준 검증 실패 (@Valid 등) - 프레임워크가 자동으로 던지는 예외
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining(", "));

        log.warn("요청 검증 실패: {}", message);

        CommonErrorCode errorCode = CommonErrorCode.INVALID_REQUEST;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.fail(errorCode, message));
    }

    // JSON 형식이 깨졌거나, @RequestBody인데 바디 자체가 비어있는 경우
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.warn("요청 본문 파싱 실패: {}", ex.getMessage());

        CommonErrorCode errorCode = CommonErrorCode.INVALID_REQUEST;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.fail(errorCode, "요청 본문 형식이 올바르지 않습니다"));
    }

    // 경로변수/쿼리파라미터의 타입이 안 맞는 경우 (예: Long 자리에 문자열이 옴)
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String message = String.format("%s 값의 형식이 올바르지 않습니다", ex.getPropertyName());
        log.warn("타입 불일치: {}", ex.getMessage());

        CommonErrorCode errorCode = CommonErrorCode.INVALID_REQUEST;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.fail(errorCode, message));
    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }

    // 특정 도메인에 속하지 않는 일반적인 비즈니스 에러 (잘못된 요청 등)
    @ExceptionHandler(CommonBusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommonBusinessException(CommonBusinessException e) {
        log.error("일반 에러: {}", e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.fail(e.getErrorCode(), e.getErrorCode().getMessage()));
    }

    // 팀센터 쪽 처리 중 발생한 에러
    @ExceptionHandler(TcBusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleTcBusinessException(TcBusinessException e) {
        log.error("팀센터 에러: {}", e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.fail(e.getErrorCode(), e.getErrorCode().getMessage()));
    }

    // ERP가 비즈니스적으로 거부한 경우
    @ExceptionHandler(ErpBusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleErpBusinessException(ErpBusinessException e) {
        log.error("ERP 비즈니스 에러 | code={}, erpCode={}, erpMessage={}",
                e.getErrorCode().getCode(), e.getErpCode(), e.getErpMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.fail(
                        e.getErrorCode(),
                        e.getErrorCode().getMessage(),
                        e.getErpCode(),
                        e.getErpMessage()));
    }

    // ERP 서버 자체가 응답하지 않거나 통신에 실패한 경우
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ApiResponse<Void>> handleRestClientException(RestClientException e) {
        log.error("ERP 통신 실패: {}", e.getMessage(), e);
        ErpErrorCode errorCode = ErpErrorCode.ERP_COMMUNICATION_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.fail(errorCode, errorCode.getMessage()));
    }

    // 그 외 예상치 못한 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("예상치 못한 에러: {}", e.getMessage(), e);
        CommonErrorCode errorCode = CommonErrorCode.INTERNAL_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.fail(errorCode, errorCode.getMessage()));
    }
}