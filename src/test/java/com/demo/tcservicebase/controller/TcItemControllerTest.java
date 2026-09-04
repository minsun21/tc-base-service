package com.demo.tcservicebase.controller;

import com.demo.tcservicebase.service.TcItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
}