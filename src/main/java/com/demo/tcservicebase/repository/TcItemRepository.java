package com.demo.tcservicebase.repository;

import com.demo.tcservicebase.domain.tc.ExampleTcMapper;
import com.demo.tcservicebase.domain.tc.dto.ItemDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * ExampleTcMapper 하나만 그대로 감싸는 단순 Repository 예제.
 * (여러 Mapper를 조합하는 예제는 ItemStockRepository 참고)
 */
@Repository
@RequiredArgsConstructor
public class TcItemRepository {

    private final ExampleTcMapper exampleTcMapper;

    public String findItemName(String itemId) {
        return exampleTcMapper.findItemNameById(itemId);
    }

    public ItemDetail findItemDetail(String itemId) {
        return exampleTcMapper.findItemDetailById(itemId);
    }
}
