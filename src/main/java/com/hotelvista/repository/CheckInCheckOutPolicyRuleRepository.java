package com.hotelvista.repository;

import com.hotelvista.model.CheckInCheckOutPolicyRule;
import com.hotelvista.model.enums.RuleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckInCheckOutPolicyRuleRepository extends JpaRepository<CheckInCheckOutPolicyRule, Long> {
    List<CheckInCheckOutPolicyRule> findByPolicyId(Long policyId);

    List<CheckInCheckOutPolicyRule> findByType(RuleType type);
}

