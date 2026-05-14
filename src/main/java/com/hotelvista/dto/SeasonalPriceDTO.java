package com.hotelvista.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeasonalPriceDTO {
    private Integer id;

    @NotBlank
    private String seasonName;

    @NotNull
    private Double priceMultiplier;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String description;

    @NotEmpty
    private Set<@NotBlank String> roomTypeIds = new HashSet<>();
}

