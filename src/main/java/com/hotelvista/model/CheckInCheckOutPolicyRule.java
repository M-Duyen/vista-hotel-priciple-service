package com.hotelvista.model;

import com.hotelvista.model.enums.RuleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "policy_rule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInCheckOutPolicyRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RuleType type;

    private LocalTime startTime;
    private LocalTime endTime;

    private Double surchargePercentage;

    @Column(name = "day_charge")
    private Boolean dayCharge;

    private Integer freeForMinRankLevel;

    @Column(name = "policy_id")
    private Long policyId;
}

