package com.demo.tcservicebase.domain.erp;

import com.demo.tcservicebase.domain.erp.dto.MaterialStockDetail;

public interface ExampleErpMapper {
    Integer findStockQuantityByItemCode(String itemCode);

    // resultType을 DTO로 지정해서 여러 컬럼을 한 번에 객체로 받는 예제
    MaterialStockDetail findStockDetailByItemCode(String itemCode);
}