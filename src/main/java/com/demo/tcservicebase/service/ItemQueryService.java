package com.demo.tcservicebase.service;

import com.demo.tcservicebase.domain.erp.ExampleErpMapper;
import com.demo.tcservicebase.domain.tc.ExampleTcMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ExampleTcMapper를 실제로 쓰는 예제 서비스.
 * MyBatis 도입 후 Mapper를 Service에서 어떻게 주입받아 쓰는지 보여주는 용도.
 */
@Service
@RequiredArgsConstructor
public class ItemQueryService {

    private final ExampleTcMapper exampleTcMapper;     // tcSqlSessionFactory 사용
    private final ExampleErpMapper exampleErpMapper;   // erpSqlSessionFactory 사용

    public String findItemName(String itemId) {
        return exampleTcMapper.findItemNameById(itemId);
    }

    /**
     * 팀센터 DB에서 품목명을, ERP DB에서 재고 수량을 각각 조회해서 하나로 합치는 예제.
     * 서로 다른 데이터소스를 쓰는 Mapper 2개를 한 서비스에서 같이 주입받아 쓰는 방식을 보여줌.
     */
    public ItemStockInfo getItemStockInfo(String itemId) {
        String itemName = exampleTcMapper.findItemNameById(itemId);
        Integer stockQty = exampleErpMapper.findStockQuantityByItemCode(itemId);

        return ItemStockInfo.builder()
                .itemId(itemId)
                .itemName(itemName)
                .stockQty(stockQty)
                .build();
    }

    @Getter
    @Builder
    public static class ItemStockInfo {
        private String itemId;
        private String itemName;
        private Integer stockQty;
    }
}