package com.demo.tcservicebase.service;

import com.demo.tcservicebase.common.exception.CommonBusinessException;
import com.demo.tcservicebase.common.exception.CommonErrorCode;
import com.demo.tcservicebase.common.exception.ErpBusinessException;
import com.demo.tcservicebase.common.exception.ErpErrorCode;
import com.demo.tcservicebase.common.exception.TcBusinessException;
import com.demo.tcservicebase.common.exception.TcErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

/**
 * TcItemService의 분기 로직(정상/일반 오류/팀센터 오류/ERP 오류) 단위테스트.
 */
class TcItemServiceTest {

    private final TcItemService tcItemService = new TcItemService();

    @Test
    void itemId가_null이면_CommonBusinessException을_던진다() {
        CommonBusinessException ex = catchThrowableOfType(
                CommonBusinessException.class, () -> tcItemService.getItemName(null));

        assertThat(ex.getErrorCode()).isEqualTo(CommonErrorCode.INVALID_REQUEST);
    }

    @Test
    void itemId가_공백이면_CommonBusinessException을_던진다() {
        CommonBusinessException ex = catchThrowableOfType(
                CommonBusinessException.class, () -> tcItemService.getItemName("   "));

        assertThat(ex.getErrorCode()).isEqualTo(CommonErrorCode.INVALID_REQUEST);
    }

    @Test
    void 등록되지_않은_itemId면_TcBusinessException을_던진다() {
        TcBusinessException ex = catchThrowableOfType(
                TcBusinessException.class, () -> tcItemService.getItemName("ITEM-9999"));

        assertThat(ex.getErrorCode()).isEqualTo(TcErrorCode.ITEM_NOT_FOUND);
    }

    @Test
    void 등록된_itemId면_품목명을_반환한다() {
        String itemName = tcItemService.getItemName("ITEM-0001");

        assertThat(itemName).isEqualTo("볼트 M8");
    }

    @Test
    void ERP에_매핑된_에러코드로_응답하면_매핑된_ErpErrorCode를_사용한다() {
        ErpBusinessException ex = catchThrowableOfType(
                ErpBusinessException.class, () -> tcItemService.sendItemToErp("ITEM-0031"));

        assertThat(ex.getErrorCode()).isEqualTo(ErpErrorCode.ERP_ITEM_NOT_FOUND);
        assertThat(ex.getErpCode()).isEqualTo("E0031");
        assertThat(ex.getErpMessage()).isEqualTo("존재하지 않는 품목입니다");
    }

    @Test
    void ERP에_매핑되지_않은_에러코드로_응답하면_기본_ErpErrorCode를_사용한다() {
        ErpBusinessException ex = catchThrowableOfType(
                ErpBusinessException.class, () -> tcItemService.sendItemToErp("ITEM-9999"));

        assertThat(ex.getErrorCode()).isEqualTo(ErpErrorCode.ERP_INTERFACE_ERROR);
        assertThat(ex.getErpCode()).isEqualTo("E9999");
    }

    @Test
    void 그_외_itemId는_예외없이_정상_종료된다() {
        assertThatCode(() -> tcItemService.sendItemToErp("ITEM-0001")).doesNotThrowAnyException();
    }
}
