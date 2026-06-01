package com.hotelvista.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyRateCalculationDTO {
    private String roomTypeId;
    private Double basePrice;
    private Integer hours;
    private Integer basePercentage;
    private Boolean weekend;
    private Boolean isWeekend;
    private Double weekendSurcharge;
    private Double totalPercentage;
    private Double totalAmount;
    private List<String> breakdown = new ArrayList<>();
}

