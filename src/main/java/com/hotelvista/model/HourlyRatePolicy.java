package com.hotelvista.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "hourly_rate_policy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyRatePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_name", nullable = false, length = 255)
    private String policyName;

    @Column(name = "weekend_surcharge", nullable = false)
    private Double weekendSurcharge;

    @ElementCollection
    @CollectionTable(name = "policy_weekend_days", joinColumns = @JoinColumn(name = "policy_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private Set<DayOfWeek> weekendDays = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "policy_base_rates", joinColumns = @JoinColumn(name = "policy_id"))
    @MapKeyColumn(name = "hours_duration")
    @Column(name = "percentage")
    private Map<Integer, Double> baseRates = new HashMap<>();
}

