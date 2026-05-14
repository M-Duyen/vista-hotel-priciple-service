package com.hotelvista.mapper;

import com.hotelvista.dto.HourlyRatePolicyDTO;
import com.hotelvista.dto.RoomTypePromotionDTO;
import com.hotelvista.dto.SeasonalPriceDTO;
import com.hotelvista.model.HourlyRatePolicy;
import com.hotelvista.model.RoomTypePromotion;
import com.hotelvista.model.SeasonalPrice;

import java.util.HashSet;

public final class PricingRuleMapper {
    private PricingRuleMapper() {
    }

    public static RoomTypePromotionDTO toDto(RoomTypePromotion entity) {
        if (entity == null) {
            return null;
        }
        return new RoomTypePromotionDTO(
                entity.getId(),
                entity.getRoomTypeId(),
                entity.getPromotionId(),
                entity.getDiscountValue(),
                entity.getStartDate(),
                entity.getEndDate()
        );
    }

    public static RoomTypePromotion toEntity(RoomTypePromotionDTO dto) {
        if (dto == null) {
            return null;
        }
        return new RoomTypePromotion(
                dto.getId(),
                dto.getRoomTypeId(),
                dto.getPromotionId(),
                dto.getDiscountValue(),
                dto.getStartDate(),
                dto.getEndDate()
        );
    }

    public static SeasonalPriceDTO toDto(SeasonalPrice entity) {
        if (entity == null) {
            return null;
        }
        return new SeasonalPriceDTO(
                entity.getId(),
                entity.getSeasonName(),
                entity.getPriceMultiplier(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getDescription(),
                entity.getRoomTypeIds() == null ? new HashSet<>() : new HashSet<>(entity.getRoomTypeIds())
        );
    }

    public static SeasonalPrice toEntity(SeasonalPriceDTO dto) {
        if (dto == null) {
            return null;
        }
        SeasonalPrice seasonalPrice = new SeasonalPrice(
                dto.getId(),
                dto.getSeasonName(),
                dto.getPriceMultiplier(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getDescription(),
                dto.getRoomTypeIds() == null ? new HashSet<>() : new HashSet<>(dto.getRoomTypeIds())
        );
        if (seasonalPrice.getRoomTypeIds() == null) {
            seasonalPrice.setRoomTypeIds(new HashSet<>());
        }
        return seasonalPrice;
    }

    public static HourlyRatePolicyDTO toDto(HourlyRatePolicy entity) {
        if (entity == null) {
            return null;
        }
        return new HourlyRatePolicyDTO(
                entity.getId(),
                entity.getPolicyName(),
                entity.getWeekendSurcharge(),
                entity.getWeekendDays(),
                entity.getBaseRates()
        );
    }

    public static HourlyRatePolicy toEntity(HourlyRatePolicyDTO dto) {
        if (dto == null) {
            return null;
        }
        HourlyRatePolicy policy = new HourlyRatePolicy(
                dto.getId(),
                dto.getPolicyName(),
                dto.getWeekendSurcharge(),
                dto.getWeekendDays(),
                dto.getBaseRates()
        );
        if (policy.getWeekendDays() == null) {
            policy.setWeekendDays(new java.util.HashSet<>());
        }
        if (policy.getBaseRates() == null) {
            policy.setBaseRates(new java.util.HashMap<>());
        }
        return policy;
    }
}

