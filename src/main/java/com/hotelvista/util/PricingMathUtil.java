package com.hotelvista.util;

import com.hotelvista.model.enums.DiscountType;

public final class PricingMathUtil {
    private PricingMathUtil() {
    }

    public static double applyDiscount(double amount, DiscountType type, double discountValue) {
        if (type == DiscountType.FIXED) {
            return clampToZero(amount - discountValue);
        }
        return clampToZero(amount - (amount * discountValue / 100.0));
    }

    public static double clampToZero(double amount) {
        return Math.max(0.0, amount);
    }
}

