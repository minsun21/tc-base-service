package com.demo.tcservicebase.repository;

import com.demo.tcservicebase.domain.erp.ExampleErpMapper;
import com.demo.tcservicebase.domain.erp.dto.MaterialStockDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * ExampleErpMapper 하나만 그대로 감싸는 단순 Repository 예제.
 * (여러 Mapper를 조합하는 예제는 ItemStockRepository 참고)
 */
@Repository
@RequiredArgsConstructor
public class ErpMaterialStockRepository {

    private final ExampleErpMapper exampleErpMapper;

    public Integer findStockQuantity(String itemCode) {
        return exampleErpMapper.findStockQuantityByItemCode(itemCode);
    }

    public MaterialStockDetail findStockDetail(String itemCode) {
        return exampleErpMapper.findStockDetailByItemCode(itemCode);
    }
}
