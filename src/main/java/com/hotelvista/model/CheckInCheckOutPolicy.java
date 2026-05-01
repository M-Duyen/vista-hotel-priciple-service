package com.hotelvista.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "check_in_out_policy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInCheckOutPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "standard_check_in_time", nullable = false)
    private LocalTime standardCheckInTime = LocalTime.of(14, 0);

    @Column(name = "standard_check_out_time", nullable = false)
    private LocalTime standardCheckOutTime = LocalTime.of(12, 0);
}

