package com.demo.tcservicebase.service;


import com.demo.tcservicebase.domain.erp.ExampleErpMapper;
import com.demo.tcservicebase.domain.tc.ExampleTcMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * 실제 DB 없이 Mapper를 Mock으로 대체해서 Service 로직만 검증하는 단위테스트.
 * Mapper의 실제 SQL 동작 자체는 DB가 필요하므로 별도의 통합테스트에서 다룸.
 */
@ExtendWith(MockitoExtension.class)
class ItemQueryServiceTest {

    @Mock
    private ExampleTcMapper exampleTcMapper;

    @InjectMocks
    private ItemQueryService itemQueryService;

    @Mock
    private ExampleErpMapper exampleErpMapper;

    @Test
    void 아이템_ID로_품목명을_조회한다() {
        when(exampleTcMapper.findItemNameById("ITEM-0001")).thenReturn("볼트 M8");

        String result = itemQueryService.findItemName("ITEM-0001");

        assertThat(result).isEqualTo("볼트 M8");
    }


    @Test
    void 팀센터_품목명과_ERP_재고를_합쳐서_반환한다() {
        when(exampleTcMapper.findItemNameById("ITEM-0001")).thenReturn("볼트 M8");
        when(exampleErpMapper.findStockQuantityByItemCode("ITEM-0001")).thenReturn(150);

        ItemQueryService.ItemStockInfo result = itemQueryService.getItemStockInfo("ITEM-0001");

        assertThat(result.getItemName()).isEqualTo("볼트 M8");
        assertThat(result.getStockQty()).isEqualTo(150);
    }
}