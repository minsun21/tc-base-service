package com.demo.tcservicebase.service;

import com.demo.tcservicebase.repository.ErpMaterialStockRepository;
import com.demo.tcservicebase.repository.ItemStockRepository;
import com.demo.tcservicebase.repository.TcItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Mapper를 직접 주입받는 대신 Repository를 거쳐서 조회하는 예제.
 * (Mapper를 직접 주입받아 쓰는 예제는 ItemQueryService 참고)
 */
@Service
@RequiredArgsConstructor
public class ItemStockService {

    private final TcItemRepository tcItemRepository;
    private final ErpMaterialStockRepository erpMaterialStockRepository;
    private final ItemStockRepository itemStockRepository;

    public String getItemName(String itemId) {
        return tcItemRepository.findItemName(itemId);
    }

    public Integer getStockQuantity(String itemCode) {
        return erpMaterialStockRepository.findStockQuantity(itemCode);
    }

    public ItemStockRepository.ItemStockDetail getItemStockDetail(String itemId) {
        return itemStockRepository.findItemStockDetail(itemId);
    }
}
