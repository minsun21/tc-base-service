package com.demo.tcservicebase.controller;

import com.demo.tcservicebase.common.dto.ApiResponse;
import com.demo.tcservicebase.dto.ItemCreateRequest;
import com.demo.tcservicebase.service.TcItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class TcItemController {

    private final TcItemService tcItemService;

    // @Valid 예제 - itemId/itemName을 비워서 요청하면 검증 실패 응답을 확인할 수 있음
    @PostMapping
    public ApiResponse<Void> createItem(@Valid @RequestBody ItemCreateRequest request) {
        // 실제로는 여기서 팀센터에 아이템을 생성하는 로직이 들어감
        return ApiResponse.success(null);
    }

    // 일반 오류 예제 (itemId 누락) - 쿼리 파라미터로 호출, 값을 안 넘기면 CommonErrorCode.INVALID_REQUEST 발생
    // 팀센터 에러 예제 (itemId를 넘기면 정상적으로 이쪽 흐름을 탐)
    @GetMapping
    public ApiResponse<String> getItemByQuery(@RequestParam(required = false) String itemId) {
        String itemName = tcItemService.getItemName(itemId);
        return ApiResponse.success(itemName);
    }

    // 팀센터 에러 예제
    @GetMapping("/{itemId}")
    public ApiResponse<String> getItem(@PathVariable String itemId) {
        String itemName = tcItemService.getItemName(itemId);
        return ApiResponse.success(itemName);
    }

    // ERP 에러 예제 (매핑된 코드 / 매핑 안 된 코드)
    @GetMapping("/{itemId}/erp-send")
    public ApiResponse<Void> sendItemToErp(@PathVariable String itemId) {
        tcItemService.sendItemToErp(itemId);
        return ApiResponse.success(null);
    }
}