package com.demo.tcservicebase.common.exception;

import com.demo.tcservicebase.common.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.beans.PropertyChangeEvent;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 스프링 컨텍스트 없이 GlobalExceptionHandler의 각 핸들러 메소드를 직접 호출해서
 * 상태코드/응답바디 매핑이 올바른지 검증하는 단위테스트.
 * (MethodArgumentNotValidException / HttpMessageNotReadableException은
 * TcItemControllerTest에서 실제 HTTP 요청으로 이미 검증하므로 여기서는 다루지 않음)
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleTypeMismatch는_프로퍼티명을_포함한_400을_반환한다() {
        PropertyChangeEvent event = new PropertyChangeEvent(new Object(), "itemId", null, "abc");
        TypeMismatchException ex = new TypeMismatchException(event, Long.class);

        ResponseEntity<Object> response = handler.handleTypeMismatch(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ApiResponse<?> body = (ApiResponse<?>) response.getBody();
        assertThat(body.getError().getCode()).isEqualTo("COMMON_400");
        assertThat(body.getError().getMessage()).contains("itemId");
    }

    @Test
    void handleCommonBusinessException은_에러코드에_맞는_상태와_바디를_반환한다() {
        ResponseEntity<ApiResponse<Void>> response =
                handler.handleCommonBusinessException(new CommonBusinessException(CommonErrorCode.UNAUTHORIZED));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getError().getCode()).isEqualTo("COMMON_401");
    }

    @Test
    void handleTcBusinessException은_에러코드에_맞는_상태와_바디를_반환한다() {
        ResponseEntity<ApiResponse<Void>> response =
                handler.handleTcBusinessException(new TcBusinessException(TcErrorCode.ITEM_NOT_FOUND));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getError().getCode()).isEqualTo("TC_404");
    }

    @Test
    void handleErpBusinessException은_ERP_원본코드와_메시지를_그대로_보존한다() {
        ErpBusinessException ex = new ErpBusinessException(ErpErrorCode.ERP_ITEM_NOT_FOUND, "E0031", "존재하지 않는 품목입니다");

        ResponseEntity<ApiResponse<Void>> response = handler.handleErpBusinessException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ApiResponse.ErrorDetail error = response.getBody().getError();
        assertThat(error.getCode()).isEqualTo("ERP_404");
        assertThat(error.getErpCode()).isEqualTo("E0031");
        assertThat(error.getErpMessage()).isEqualTo("존재하지 않는 품목입니다");
    }

    @Test
    void handleRestClientException은_ERP_통신불가_503을_반환한다() {
        ResponseEntity<ApiResponse<Void>> response =
                handler.handleRestClientException(new RestClientException("connection refused"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody().getError().getCode()).isEqualTo("ERP_503");
    }

    @Test
    void handleException은_예상치_못한_예외를_500으로_반환한다() {
        ResponseEntity<ApiResponse<Void>> response = handler.handleException(new RuntimeException("boom"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getError().getCode()).isEqualTo("COMMON_500");
    }
}
