package com.demo.tcservicebase.domain.tc;

import com.demo.tcservicebase.domain.tc.dto.ItemDetail;

public interface ExampleTcMapper {
    String findItemNameById(String itemId);

    // resultType을 DTO로 지정해서 여러 컬럼을 한 번에 객체로 받는 예제
    ItemDetail findItemDetailById(String itemId);
}
