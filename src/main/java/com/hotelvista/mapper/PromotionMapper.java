package com.hotelvista.mapper;

import com.hotelvista.dto.PromotionDTO;
import com.hotelvista.dto.PromotionTypeDTO;
import com.hotelvista.model.Promotion;
import com.hotelvista.model.PromotionType;

public final class PromotionMapper {
    private PromotionMapper() {
    }

    public static PromotionTypeDTO toDto(PromotionType entity) {
        if (entity == null) {
            return null;
        }
        return new PromotionTypeDTO(entity.getPromotionTypeId(), entity.getPromotionTypeName(), entity.getDescription());
    }

    public static PromotionType toEntity(PromotionTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        return new PromotionType(dto.getPromotionTypeId(), dto.getPromotionTypeName(), dto.getDescription());
    }

    public static PromotionDTO toDto(Promotion entity) {
        if (entity == null) {
            return null;
        }
        return new PromotionDTO(
                entity.getPromotionId(),
                entity.getPromotionName(),
                entity.getDescription(),
                entity.getDiscountType(),
                entity.isActive(),
                entity.getAdminId(),
                entity.getPromotionTypeId()
        );
    }

    public static Promotion toEntity(PromotionDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Promotion(
                dto.getPromotionId(),
                dto.getPromotionName(),
                dto.getDescription(),
                dto.getDiscountType(),
                dto.isActive(),
                dto.getAdminId(),
                dto.getPromotionTypeId()
        );
    }
}

