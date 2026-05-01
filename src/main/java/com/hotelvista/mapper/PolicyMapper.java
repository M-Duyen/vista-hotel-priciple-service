package com.hotelvista.mapper;

import com.hotelvista.dto.CheckInCheckOutPolicyDTO;
import com.hotelvista.dto.CheckInCheckOutPolicyRuleDTO;
import com.hotelvista.model.CheckInCheckOutPolicy;
import com.hotelvista.model.CheckInCheckOutPolicyRule;

public final class PolicyMapper {
    private PolicyMapper() {
    }

    public static CheckInCheckOutPolicyDTO toDto(CheckInCheckOutPolicy entity) {
        if (entity == null) {
            return null;
        }
        return new CheckInCheckOutPolicyDTO(
                entity.getId(),
                entity.getName(),
                entity.getStandardCheckInTime(),
                entity.getStandardCheckOutTime()
        );
    }

    public static CheckInCheckOutPolicy toEntity(CheckInCheckOutPolicyDTO dto) {
        if (dto == null) {
            return null;
        }
        return new CheckInCheckOutPolicy(
                dto.getId(),
                dto.getName(),
                dto.getStandardCheckInTime(),
                dto.getStandardCheckOutTime()
        );
    }

    public static CheckInCheckOutPolicyRuleDTO toDto(CheckInCheckOutPolicyRule entity) {
        if (entity == null) {
            return null;
        }
        return new CheckInCheckOutPolicyRuleDTO(
                entity.getId(),
                entity.getType(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getSurchargePercentage(),
                entity.getDayCharge(),
                entity.getFreeForMinRankLevel(),
                entity.getPolicyId()
        );
    }

    public static CheckInCheckOutPolicyRule toEntity(CheckInCheckOutPolicyRuleDTO dto) {
        if (dto == null) {
            return null;
        }
        return new CheckInCheckOutPolicyRule(
                dto.getId(),
                dto.getType(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getSurchargePercentage(),
                dto.getDayCharge(),
                dto.getFreeForMinRankLevel(),
                dto.getPolicyId()
        );
    }
}

