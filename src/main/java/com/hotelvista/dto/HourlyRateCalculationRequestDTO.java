package com.hotelvista.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyRateCalculationRequestDTO {
    @NotBlank
    private String roomTypeId;

    @NotNull
    @Positive
    private Double basePrice;

    @NotNull
    @Positive
    private Integer hours;

    @NotNull
    private LocalDateTime checkInDateTime;

    private Long policyId;
}

