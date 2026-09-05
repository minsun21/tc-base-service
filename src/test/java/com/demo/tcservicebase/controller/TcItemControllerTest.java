package com.demo.tcservicebase.controller;

import com.demo.tcservicebase.common.exception.ErpBusinessException;
import com.demo.tcservicebase.common.exception.ErpErrorCode;
import com.demo.tcservicebase.common.exception.TcBusinessException;
import com.demo.tcservicebase.common.exception.TcErrorCode;
import com.demo.tcservicebase.service.TcItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller ~ GlobalExceptionHandler까지 전체 흐름이 우리 ApiResponse 형식대로
 * 응답되는지 검증하는 예제 테스트.
 */
@WebMvcTest(TcItemController.class)
class TcItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TcItemService tcItemService;

    @Test
    void 정상_조회는_success_true로_응답한다() throws Exception {
        when(tcItemService.getItemName("ITEM-0001")).thenReturn("볼트 M8");

        mockMvc.perform(get("/api/items/ITEM-0001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("볼트 M8"));
    }

    @Test
    void 검증_실패시_COMMON_400과_400상태코드로_응답한다() throws Exception {
        String invalidBody = "{\"itemId\":\"\",\"itemName\":\"\"}";

        mockMvc.perform(post("/api/items")
                        .contentType("application/json")
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("COMMON_400"));
    }

    @Test
    void 요청_본문이_깨지면_COMMON_400으로_응답한다() throws Exception {
        String brokenJson = "{ 이거 잘못된 json";

        mockMvc.perform(post("/api/items")
                        .contentType("application/json")
                        .content(brokenJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("COMMON_400"));
    }

    @Test
    void 유효한_값으로_생성_요청하면_200으로_응답한다() throws Exception {
        String validBody = "{\"itemId\":\"ITEM-0001\",\"itemName\":\"볼트 M8\"}";

        mockMvc.perform(post("/api/items")
                        .contentType("application/json")
                        .content(validBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void 쿼리파라미터로_itemId를_안넘기면_COMMON_400으로_응답한다() throws Exception {
        when(tcItemService.getItemName(isNull()))
                .thenThrow(new com.demo.tcservicebase.common.exception.CommonBusinessException(
                        com.demo.tcservicebase.common.exception.CommonErrorCode.INVALID_REQUEST));

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("COMMON_400"));
    }

    @Test
    void 등록되지_않은_itemId로_조회하면_TC_404로_응답한다() throws Exception {
        when(tcItemService.getItemName("ITEM-9999")).thenThrow(new TcBusinessException(TcErrorCode.ITEM_NOT_FOUND));

        mockMvc.perform(get("/api/items/ITEM-9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("TC_404"));
    }

    @Test
    void ERP_매핑된_에러코드면_ERP_404로_응답하고_원본코드를_함께_내려준다() throws Exception {
        doThrow(new ErpBusinessException(ErpErrorCode.ERP_ITEM_NOT_FOUND, "E0031", "존재하지 않는 품목입니다"))
                .when(tcItemService).sendItemToErp("ITEM-0031");

        mockMvc.perform(get("/api/items/ITEM-0031/erp-send"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("ERP_404"))
                .andExpect(jsonPath("$.error.erpCode").value("E0031"));
    }

    @Test
    void ERP_매핑안된_에러코드면_ERP_500으로_응답한다() throws Exception {
        doThrow(new ErpBusinessException(ErpErrorCode.ERP_INTERFACE_ERROR, "E9999", "알 수 없는 ERP 내부 오류"))
                .when(tcItemService).sendItemToErp("ITEM-9999");

        mockMvc.perform(get("/api/items/ITEM-9999/erp-send"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("ERP_500"));
    }

    @Test
    void ERP_전송이_성공하면_200으로_응답한다() throws Exception {
        mockMvc.perform(get("/api/items/ITEM-0001/erp-send"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}