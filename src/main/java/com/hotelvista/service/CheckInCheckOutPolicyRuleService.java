package com.hotelvista.service;

import com.hotelvista.dto.CheckInCheckOutPolicyRuleDTO;
import com.hotelvista.exception.ResourceNotFoundException;
import com.hotelvista.mapper.PolicyMapper;
import com.hotelvista.model.CheckInCheckOutPolicyRule;
import com.hotelvista.repository.CheckInCheckOutPolicyRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CheckInCheckOutPolicyRuleService {
    private final CheckInCheckOutPolicyRuleRepository repository;

    public CheckInCheckOutPolicyRuleService(CheckInCheckOutPolicyRuleRepository repository) {
        this.repository = repository;
    }

    public List<CheckInCheckOutPolicyRuleDTO> findAll() {
        return repository.findAll().stream().map(PolicyMapper::toDto).toList();
    }

    public CheckInCheckOutPolicyRuleDTO findById(Long id) {
        return repository.findById(id).map(PolicyMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Policy rule not found: " + id));
    }

    public List<CheckInCheckOutPolicyRuleDTO> findByPolicyId(Long policyId) {
        return repository.findByPolicyId(policyId).stream().map(PolicyMapper::toDto).toList();
    }

    @Transactional
    public CheckInCheckOutPolicyRuleDTO save(CheckInCheckOutPolicyRuleDTO dto) {
        CheckInCheckOutPolicyRule saved = repository.save(PolicyMapper.toEntity(dto));
        return PolicyMapper.toDto(saved);
    }

    @Transactional
    public CheckInCheckOutPolicyRuleDTO update(Long id, CheckInCheckOutPolicyRuleDTO dto) {
        CheckInCheckOutPolicyRule existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy rule not found: " + id));
        existing.setType(dto.getType());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        existing.setSurchargePercentage(dto.getSurchargePercentage());
        existing.setDayCharge(dto.getDayCharge());
        existing.setFreeForMinRankLevel(dto.getFreeForMinRankLevel());
        existing.setPolicyId(dto.getPolicyId());
        return PolicyMapper.toDto(repository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Policy rule not found: " + id);
        }
        repository.deleteById(id);
    }
}

