package com.demo.tcservicebase.service;

import com.demo.tcservicebase.common.aop.InterfaceLog;
import com.demo.tcservicebase.common.aop.LoggableInterface;
import com.demo.tcservicebase.common.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TcItemService {

    // ── 일반 오류 예제 (특정 도메인에 속하지 않는 에러) ──
    @LoggableInterface(name = "팀센터 아이템 조회")
    public String getItemName(String itemId) {

        // 요청 자체가 잘못된 경우 (예: itemId 누락) - 팀센터/ERP와 무관한 일반 오류
        if (!StringUtils.hasText(itemId)) {
            throw new CommonBusinessException(CommonErrorCode.INVALID_REQUEST);
        }

        InterfaceLog.step("팀센터 SOA Query 서비스 호출 중");

        // 실제로는 팀센터 SOA Query 서비스를 호출해서 조회하는 부분
        if (!"ITEM-0001".equals(itemId)) {
            throw new TcBusinessException(TcErrorCode.ITEM_NOT_FOUND);
        }

        return "볼트 M8";
    }

    // ── ERP 에러 예제 (매핑된 코드 / 매핑 안 된 코드) ──
    @LoggableInterface(name = "ERP 자재 전송")
    public void sendItemToErp(String itemId) {

        // 매핑에 등록된 에러 코드로 응답하는 경우 (E0031 → ErpErrorCode.ERP_ITEM_NOT_FOUND)
        if ("ITEM-0031".equals(itemId)) {
            String rawErpCode = "E0031";
            String rawErpMessage = "존재하지 않는 품목입니다";
            ErpErrorCode mappedCode = ErpErrorCode.resolve(rawErpCode);
            throw new ErpBusinessException(mappedCode, rawErpCode, rawErpMessage);
        }

        // 매핑에 등록되지 않은, 처음 보는 에러 코드로 응답하는 경우
        if ("ITEM-9999".equals(itemId)) {
            String rawErpCode = "E9999";
            String rawErpMessage = "알 수 없는 ERP 내부 오류";
            ErpErrorCode mappedCode = ErpErrorCode.resolve(rawErpCode);  // 매핑 없음 → 기본값 반환
            throw new ErpBusinessException(mappedCode, rawErpCode, rawErpMessage);
        }

        // 성공 시 별도로 돌려줄 데이터가 없다면 그냥 반환 없이 종료
    }
}
