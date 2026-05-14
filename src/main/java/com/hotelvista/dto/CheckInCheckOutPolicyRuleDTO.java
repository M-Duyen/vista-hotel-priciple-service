package com.hotelvista.dto;

import com.hotelvista.model.enums.RuleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInCheckOutPolicyRuleDTO {
    private Long id;

    @NotNull
    private RuleType type;

    private LocalTime startTime;
    private LocalTime endTime;

    private Double surchargePercentage;
    private Boolean dayCharge;
    private String freeForMinRankLevel;
    private Long policyId;
}

