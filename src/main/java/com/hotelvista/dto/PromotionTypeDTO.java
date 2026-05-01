package com.hotelvista.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionTypeDTO {
    @NotBlank
    private String promotionTypeId;

    @NotBlank
    private String promotionTypeName;

    private String description;
}

