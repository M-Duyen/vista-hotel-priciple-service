package com.hotelvista.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypePromotionDTO {
    private Long id;

    @NotBlank
    private String roomTypeId;

    @NotBlank
    private String promotionId;

    @NotNull
    private Double discountValue;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}

