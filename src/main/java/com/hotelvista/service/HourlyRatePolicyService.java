package com.hotelvista.service;

import com.hotelvista.dto.HourlyRatePolicyDTO;
import com.hotelvista.exception.ResourceNotFoundException;
import com.hotelvista.mapper.PricingRuleMapper;
import com.hotelvista.model.HourlyRatePolicy;
import com.hotelvista.repository.HourlyRatePolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HourlyRatePolicyService {
    private final HourlyRatePolicyRepository repository;

    public HourlyRatePolicyService(HourlyRatePolicyRepository repository) {
        this.repository = repository;
    }

    public List<HourlyRatePolicyDTO> findAll() {
        return repository.findAll().stream().map(PricingRuleMapper::toDto).toList();
    }

    public HourlyRatePolicyDTO findById(Long id) {
        return repository.findById(id).map(PricingRuleMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Hourly rate policy not found: " + id));
    }

    @Transactional
    public HourlyRatePolicyDTO save(HourlyRatePolicyDTO dto) {
        HourlyRatePolicy saved = repository.save(PricingRuleMapper.toEntity(dto));
        return PricingRuleMapper.toDto(saved);
    }

    @Transactional
    public HourlyRatePolicyDTO update(Long id, HourlyRatePolicyDTO dto) {
        HourlyRatePolicy existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hourly rate policy not found: " + id));
        existing.setPolicyName(dto.getPolicyName());
        existing.setWeekendSurcharge(dto.getWeekendSurcharge());
        existing.setWeekendDays(dto.getWeekendDays());
        existing.setBaseRates(dto.getBaseRates());
        return PricingRuleMapper.toDto(repository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Hourly rate policy not found: " + id);
        }
        repository.deleteById(id);
    }

    public HourlyRatePolicyDTO getDefaultPolicy() {
        return repository.findAll().stream().findFirst().map(PricingRuleMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("No hourly rate policy configured"));
    }

    public Optional<HourlyRatePolicy> findByIdOptional(Long id) {
        return repository.findById(id);
    }
}




