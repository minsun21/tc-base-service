package com.demo.tcservicebase.repository;

import com.demo.tcservicebase.domain.erp.ExampleErpMapper;
import com.demo.tcservicebase.domain.erp.dto.MaterialStockDetail;
import com.demo.tcservicebase.domain.tc.ExampleTcMapper;
import com.demo.tcservicebase.domain.tc.dto.ItemDetail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * ItemStockRepository의 조합 로직(팀센터+ERP)만 검증하는 순수 단위테스트.
 * 트랜잭션은 스프링 프록시로 동작하는 부가기능이라 여기서는 다루지 않는다.
 */
@ExtendWith(MockitoExtension.class)
class ItemStockRepositoryTest {

    @Mock
    private ExampleTcMapper exampleTcMapper;

    @Mock
    private ExampleErpMapper exampleErpMapper;

    @Test
    void 팀센터_품목정보와_ERP_재고정보를_하나로_합쳐서_반환한다() {
        when(exampleTcMapper.findItemDetailById("ITEM-0001")).thenReturn(itemDetail("ITEM-0001", "볼트 M8", "FASTENER"));
        when(exampleErpMapper.findStockDetailByItemCode("ITEM-0001")).thenReturn(stockDetail("ITEM-0001", 150, "WH-01"));

        ItemStockRepository repository = new ItemStockRepository(exampleTcMapper, exampleErpMapper);

        ItemStockRepository.ItemStockDetail result = repository.findItemStockDetail("ITEM-0001");

        assertThat(result.getItemName()).isEqualTo("볼트 M8");
        assertThat(result.getItemType()).isEqualTo("FASTENER");
        assertThat(result.getStockQty()).isEqualTo(150);
        assertThat(result.getWarehouseCode()).isEqualTo("WH-01");
    }

    @Test
    void 팀센터에_품목이_없으면_ERP는_조회하지_않고_null을_반환한다() {
        when(exampleTcMapper.findItemDetailById("NO-SUCH-ITEM")).thenReturn(null);

        ItemStockRepository repository = new ItemStockRepository(exampleTcMapper, exampleErpMapper);

        ItemStockRepository.ItemStockDetail result = repository.findItemStockDetail("NO-SUCH-ITEM");

        assertThat(result).isNull();
        verifyNoInteractions(exampleErpMapper);
    }

    @Test
    void ERP에_재고정보가_없어도_품목정보는_반환한다() {
        when(exampleTcMapper.findItemDetailById("ITEM-0002")).thenReturn(itemDetail("ITEM-0002", "너트 M8", "FASTENER"));
        when(exampleErpMapper.findStockDetailByItemCode("ITEM-0002")).thenReturn(null);

        ItemStockRepository repository = new ItemStockRepository(exampleTcMapper, exampleErpMapper);

        ItemStockRepository.ItemStockDetail result = repository.findItemStockDetail("ITEM-0002");

        assertThat(result.getItemName()).isEqualTo("너트 M8");
        assertThat(result.getStockQty()).isNull();
        assertThat(result.getWarehouseCode()).isNull();
    }

    private ItemDetail itemDetail(String itemId, String itemName, String itemType) {
        ItemDetail detail = new ItemDetail();
        detail.setItemId(itemId);
        detail.setItemName(itemName);
        detail.setItemType(itemType);
        return detail;
    }

    private MaterialStockDetail stockDetail(String materialCode, Integer stockQty, String warehouseCode) {
        MaterialStockDetail detail = new MaterialStockDetail();
        detail.setMaterialCode(materialCode);
        detail.setStockQty(stockQty);
        detail.setWarehouseCode(warehouseCode);
        return detail;
    }
}
