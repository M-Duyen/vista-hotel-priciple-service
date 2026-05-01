package com.hotelvista.service;

import com.hotelvista.dto.CheckInCheckOutPolicyDTO;
import com.hotelvista.dto.CheckInCheckOutPolicyRuleDTO;
import com.hotelvista.exception.ResourceNotFoundException;
import com.hotelvista.mapper.PolicyMapper;
import com.hotelvista.repository.CheckInCheckOutPolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CheckInCheckOutPolicyService {
    private final CheckInCheckOutPolicyRepository repository;
    private final CheckInCheckOutPolicyRuleService ruleService;

    public CheckInCheckOutPolicyService(CheckInCheckOutPolicyRepository repository,
                                       CheckInCheckOutPolicyRuleService ruleService) {
        this.repository = repository;
        this.ruleService = ruleService;
    }

    public List<CheckInCheckOutPolicyDTO> findAll() {
        return repository.findAll().stream().map(PolicyMapper::toDto).toList();
    }

    public CheckInCheckOutPolicyDTO findById(Long id) {
        return repository.findById(id).map(PolicyMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in/check-out policy not found: " + id));
    }

    public List<CheckInCheckOutPolicyRuleDTO> findRules(Long policyId) {
        return ruleService.findByPolicyId(policyId);
    }

    @Transactional
    public CheckInCheckOutPolicyDTO save(CheckInCheckOutPolicyDTO dto) {
        return PolicyMapper.toDto(repository.save(PolicyMapper.toEntity(dto)));
    }

    @Transactional
    public CheckInCheckOutPolicyDTO update(Long id, CheckInCheckOutPolicyDTO dto) {
        var existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in/check-out policy not found: " + id));
        existing.setName(dto.getName());
        existing.setStandardCheckInTime(dto.getStandardCheckInTime());
        existing.setStandardCheckOutTime(dto.getStandardCheckOutTime());
        return PolicyMapper.toDto(repository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Check-in/check-out policy not found: " + id);
        }
        repository.deleteById(id);
    }
}

