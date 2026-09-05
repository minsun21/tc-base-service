package com.demo.tcservicebase.common.dto;

import com.demo.tcservicebase.common.exception.CommonErrorCode;
import com.demo.tcservicebase.common.exception.ErpErrorCode;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    private final ObjectMapper objectMapper = new JsonMapper();

    @Test
    void success는_성공플래그와_데이터를_담고_에러는_비운다() {
        ApiResponse<String> response = ApiResponse.success("볼트 M8");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo("볼트 M8");
        assertThat(response.getError()).isNull();
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void fail은_erp정보없이_호출하면_erp필드가_null이다() {
        ApiResponse<Void> response = ApiResponse.fail(CommonErrorCode.INVALID_REQUEST, "잘못된 요청입니다");

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getData()).isNull();
        assertThat(response.getError().getCode()).isEqualTo("COMMON_400");
        assertThat(response.getError().getMessage()).isEqualTo("잘못된 요청입니다");
        assertThat(response.getError().getErpCode()).isNull();
        assertThat(response.getError().getErpMessage()).isNull();
    }

    @Test
    void fail은_erp정보를_함께_넘기면_그대로_보존한다() {
        ApiResponse<Void> response = ApiResponse.fail(
                ErpErrorCode.ERP_ITEM_NOT_FOUND, "ERP 연동 중 오류가 발생했습니다", "E0031", "존재하지 않는 품목입니다");

        assertThat(response.getError().getErpCode()).isEqualTo("E0031");
        assertThat(response.getError().getErpMessage()).isEqualTo("존재하지 않는 품목입니다");
    }

    @Test
    void success_직렬화시_error키는_JSON에_포함되지_않는다() throws Exception {
        String json = objectMapper.writeValueAsString(ApiResponse.success("x"));

        assertThat(json).contains("\"success\":true").doesNotContain("\"error\"");
    }

    @Test
    void fail_직렬화시_data키는_JSON에_포함되지_않는다() throws Exception {
        String json = objectMapper.writeValueAsString(ApiResponse.fail(CommonErrorCode.INVALID_REQUEST, "잘못된 요청"));

        assertThat(json).contains("\"success\":false").doesNotContain("\"data\"");
    }
}
