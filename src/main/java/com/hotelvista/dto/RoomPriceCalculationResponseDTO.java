package com.hotelvista.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomPriceCalculationResponseDTO {
    private String roomTypeId;
    private Double basePrice;
    private Integer seasonalPriceId;
    private Double seasonalMultiplier;
    private Double seasonalPrice;
    private List<String> appliedPromotionIds = new ArrayList<>();
    private Double totalDiscountAmount;
    private Double finalPrice;
    private List<String> breakdown = new ArrayList<>();
}

