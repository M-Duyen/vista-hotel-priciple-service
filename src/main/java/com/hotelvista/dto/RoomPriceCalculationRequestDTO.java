package com.hotelvista.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomPriceCalculationRequestDTO {
    @NotBlank
    private String roomTypeId;

    @NotNull
    @Positive
    private Double basePrice;

    @NotNull
    private LocalDate bookingDate;
}

