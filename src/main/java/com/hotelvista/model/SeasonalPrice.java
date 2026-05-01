package com.hotelvista.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "seasonal_prices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeasonalPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "season_name", nullable = false, length = 255)
    private String seasonName;

    @Column(name = "price_multiplier", nullable = false)
    private double priceMultiplier;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(length = 1000)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "seasonal_price_room_type_ids", joinColumns = @JoinColumn(name = "seasonal_price_id"))
    @Column(name = "room_type_id", nullable = false, length = 50)
    private Set<String> roomTypeIds = new HashSet<>();
}

