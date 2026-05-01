package com.hotelvista.repository;

import com.hotelvista.model.HourlyRatePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HourlyRatePolicyRepository extends JpaRepository<HourlyRatePolicy, Long> {
}

