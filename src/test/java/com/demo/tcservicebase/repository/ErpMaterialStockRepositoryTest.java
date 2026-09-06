package com.demo.tcservicebase.repository;

import com.demo.tcservicebase.domain.erp.ExampleErpMapper;
import com.demo.tcservicebase.domain.erp.dto.MaterialStockDetail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErpMaterialStockRepositoryTest {

    @Mock
    private ExampleErpMapper exampleErpMapper;

    @Test
    void findStockQuantity는_Mapper_조회결과를_그대로_반환한다() {
        when(exampleErpMapper.findStockQuantityByItemCode("ITEM-0001")).thenReturn(150);

        ErpMaterialStockRepository repository = new ErpMaterialStockRepository(exampleErpMapper);

        assertThat(repository.findStockQuantity("ITEM-0001")).isEqualTo(150);
    }

    @Test
    void findStockDetail은_Mapper_조회결과를_그대로_반환한다() {
        MaterialStockDetail detail = new MaterialStockDetail();
        detail.setMaterialCode("ITEM-0001");
        detail.setStockQty(150);
        detail.setWarehouseCode("WH-01");
        when(exampleErpMapper.findStockDetailByItemCode("ITEM-0001")).thenReturn(detail);

        ErpMaterialStockRepository repository = new ErpMaterialStockRepository(exampleErpMapper);

        assertThat(repository.findStockDetail("ITEM-0001")).isSameAs(detail);
    }
}
