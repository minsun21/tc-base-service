package com.demo.tcservicebase.domain.tc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ExampleTcMapper.findItemDetailById 조회 결과를 담는 DTO.
 * resultType으로 이 클래스를 그대로 지정하면, MyBatis가 조회 컬럼을 같은 이름의 프로퍼티에 자동으로 매핑해준다.
 * TcDataSourceConfig에서 mapUnderscoreToCamelCase(true)를 켜뒀기 때문에
 * DB 컬럼이 스네이크 케이스(item_id, item_name, item_type)여도 별도 별칭(AS) 없이 카멜케이스 필드에 매핑됨.
 */
@Getter
@Setter
@NoArgsConstructor
public class ItemDetail {

    private String itemId;
    private String itemName;
    private String itemType;
}
