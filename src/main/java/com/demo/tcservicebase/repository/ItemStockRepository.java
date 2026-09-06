package com.demo.tcservicebase.repository;

import com.demo.tcservicebase.domain.erp.ExampleErpMapper;
import com.demo.tcservicebase.domain.erp.dto.MaterialStockDetail;
import com.demo.tcservicebase.domain.tc.ExampleTcMapper;
import com.demo.tcservicebase.domain.tc.dto.ItemDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 팀센터/ERP 두 Mapper(서로 다른 데이터소스)를 한 메소드 안에서 조합하고,
 * 트랜잭션까지 함께 다루는 Repository 예제.
 * (단순히 Mapper를 그대로 위임만 하는 예제는 ItemQueryService 참고)
 */
@Repository
@RequiredArgsConstructor
public class ItemStockRepository {

    private final ExampleTcMapper exampleTcMapper;
    private final ExampleErpMapper exampleErpMapper;

    // tcTransactionManager로 트랜잭션을 걸었지만 이건 팀센터 DB 쪽 호출에만 적용됨 -
    // ERP는 별도 데이터소스라 같은 트랜잭션으로 묶이지 않으며, 두 DB를 하나의 트랜잭션으로 묶으려면 JTA 같은 분산 트랜잭션이 필요함.
    @Transactional("tcTransactionManager")
    public ItemStockDetail findItemStockDetail(String itemId) {
        ItemDetail itemDetail = exampleTcMapper.findItemDetailById(itemId);
        if (itemDetail == null) {
            return null;
        }

        MaterialStockDetail stockDetail = exampleErpMapper.findStockDetailByItemCode(itemId);

        return ItemStockDetail.builder()
                .itemId(itemDetail.getItemId())
                .itemName(itemDetail.getItemName())
                .itemType(itemDetail.getItemType())
                .stockQty(stockDetail != null ? stockDetail.getStockQty() : null)
                .warehouseCode(stockDetail != null ? stockDetail.getWarehouseCode() : null)
                .build();
    }

    @Getter
    @Builder
    public static class ItemStockDetail {
        private String itemId;
        private String itemName;
        private String itemType;
        private Integer stockQty;
        private String warehouseCode;
    }
}
