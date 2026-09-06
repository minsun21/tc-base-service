package com.demo.tcservicebase.domain.erp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ExampleErpMapper.findStockDetailByItemCode 조회 결과를 담는 DTO.
 * ErpDataSourceConfig에서 mapUnderscoreToCamelCase(true)를 켜뒀기 때문에
 * DB 컬럼이 스네이크 케이스(material_code, stock_qty, warehouse_code)여도 별도 별칭(AS) 없이 카멜케이스 필드에 매핑됨.
 */
@Getter
@Setter
@NoArgsConstructor
public class MaterialStockDetail {

    private String materialCode;
    private Integer stockQty;
    private String warehouseCode;
}
