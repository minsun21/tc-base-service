package com.demo.tcservicebase.service;

import com.demo.tcservicebase.repository.ErpMaterialStockRepository;
import com.demo.tcservicebase.repository.ItemStockRepository;
import com.demo.tcservicebase.repository.TcItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Service가 Mapper 대신 Repository를 통해 조회하는 흐름을 검증하는 단위테스트.
 */
@ExtendWith(MockitoExtension.class)
class ItemStockServiceTest {

    @Mock
    private TcItemRepository tcItemRepository;

    @Mock
    private ErpMaterialStockRepository erpMaterialStockRepository;

    @Mock
    private ItemStockRepository itemStockRepository;

    @Test
    void getItemName은_TcItemRepository를_통해_조회한다() {
        when(tcItemRepository.findItemName("ITEM-0001")).thenReturn("볼트 M8");

        ItemStockService service = new ItemStockService(tcItemRepository, erpMaterialStockRepository, itemStockRepository);

        assertThat(service.getItemName("ITEM-0001")).isEqualTo("볼트 M8");
    }

    @Test
    void getStockQuantity는_ErpMaterialStockRepository를_통해_조회한다() {
        when(erpMaterialStockRepository.findStockQuantity("ITEM-0001")).thenReturn(150);

        ItemStockService service = new ItemStockService(tcItemRepository, erpMaterialStockRepository, itemStockRepository);

        assertThat(service.getStockQuantity("ITEM-0001")).isEqualTo(150);
    }

    @Test
    void getItemStockDetail은_ItemStockRepository를_통해_조회한다() {
        ItemStockRepository.ItemStockDetail detail = ItemStockRepository.ItemStockDetail.builder()
                .itemId("ITEM-0001")
                .itemName("볼트 M8")
                .itemType("FASTENER")
                .stockQty(150)
                .warehouseCode("WH-01")
                .build();
        when(itemStockRepository.findItemStockDetail("ITEM-0001")).thenReturn(detail);

        ItemStockService service = new ItemStockService(tcItemRepository, erpMaterialStockRepository, itemStockRepository);

        assertThat(service.getItemStockDetail("ITEM-0001")).isSameAs(detail);
    }
}
