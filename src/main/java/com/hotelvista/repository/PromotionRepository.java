package com.hotelvista.repository;

import com.hotelvista.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, String> {
    List<Promotion> findByActiveTrue();

    List<Promotion> findByActiveFalse();

    List<Promotion> findByPromotionNameContainingIgnoreCase(String promotionName);

    List<Promotion> findByPromotionTypeId(String promotionTypeId);

    List<Promotion> findByPromotionIdInAndActiveTrue(Collection<String> promotionIds);
}

