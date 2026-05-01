package com.hotelvista.dto;

import com.hotelvista.model.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDTO {
    @NotBlank
    private String promotionId;

    @NotBlank
    private String promotionName;

    private String description;

    @NotNull
    private DiscountType discountType;

    private boolean active = true;

    private String adminId;

    @NotBlank
    private String promotionTypeId;
}

