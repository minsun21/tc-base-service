package com.demo.tcservicebase.repository;

import com.demo.tcservicebase.domain.tc.ExampleTcMapper;
import com.demo.tcservicebase.domain.tc.dto.ItemDetail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TcItemRepositoryTest {

    @Mock
    private ExampleTcMapper exampleTcMapper;

    @Test
    void findItemName은_Mapper_조회결과를_그대로_반환한다() {
        when(exampleTcMapper.findItemNameById("ITEM-0001")).thenReturn("볼트 M8");

        TcItemRepository repository = new TcItemRepository(exampleTcMapper);

        assertThat(repository.findItemName("ITEM-0001")).isEqualTo("볼트 M8");
    }

    @Test
    void findItemDetail은_Mapper_조회결과를_그대로_반환한다() {
        ItemDetail detail = new ItemDetail();
        detail.setItemId("ITEM-0001");
        detail.setItemName("볼트 M8");
        detail.setItemType("FASTENER");
        when(exampleTcMapper.findItemDetailById("ITEM-0001")).thenReturn(detail);

        TcItemRepository repository = new TcItemRepository(exampleTcMapper);

        assertThat(repository.findItemDetail("ITEM-0001")).isSameAs(detail);
    }
}
