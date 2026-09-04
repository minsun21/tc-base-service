package com.demo.tcservicebase.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemCreateRequest {

    @NotBlank(message = "품목 ID는 필수입니다")
    private String itemId;

    @NotBlank(message = "품목명은 필수입니다")
    private String itemName;
}
