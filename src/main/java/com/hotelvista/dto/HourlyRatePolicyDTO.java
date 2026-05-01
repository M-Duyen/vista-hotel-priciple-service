package com.hotelvista.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyRatePolicyDTO {
    private Long id;

    @NotBlank
    private String policyName;

    @NotNull
    private Double weekendSurcharge;

    private Set<DayOfWeek> weekendDays = new HashSet<>();

    private Map<Integer, Double> baseRates = new HashMap<>();
}

