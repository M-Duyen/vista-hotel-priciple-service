package com.hotelvista.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum DiscountType {
    PERCENT("Percentage (%)"),
    FIXED("Fixed Amount (VND)");

    private String discountName;
}

