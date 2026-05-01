package com.hotelvista.service;

import com.hotelvista.dto.SeasonalPriceDTO;
import com.hotelvista.exception.ResourceNotFoundException;
import com.hotelvista.mapper.PricingRuleMapper;
import com.hotelvista.model.SeasonalPrice;
import com.hotelvista.repository.SeasonalPriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SeasonalPriceService {
    private final SeasonalPriceRepository repository;

    public SeasonalPriceService(SeasonalPriceRepository repository) {
        this.repository = repository;
    }

    public List<SeasonalPriceDTO> findAll() {
        return repository.findAll().stream().map(PricingRuleMapper::toDto).toList();
    }

    public SeasonalPriceDTO findById(Integer id) {
        return repository.findById(id).map(PricingRuleMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Seasonal price not found: " + id));
    }

    public List<SeasonalPriceDTO> findApplicableByRoomTypeIdAndDate(String roomTypeId, LocalDate date) {
        return repository.findApplicableByRoomTypeIdAndDate(roomTypeId, date).stream().map(PricingRuleMapper::toDto).toList();
    }

    public List<SeasonalPrice> findApplicableEntitiesByRoomTypeIdAndDate(String roomTypeId, LocalDate date) {
        return repository.findApplicableByRoomTypeIdAndDate(roomTypeId, date);
    }

    @Transactional
    public SeasonalPriceDTO save(SeasonalPriceDTO dto) {
        SeasonalPrice saved = repository.save(PricingRuleMapper.toEntity(dto));
        return PricingRuleMapper.toDto(saved);
    }

    @Transactional
    public SeasonalPriceDTO update(Integer id, SeasonalPriceDTO dto) {
        SeasonalPrice existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seasonal price not found: " + id));
        existing.setSeasonName(dto.getSeasonName());
        existing.setPriceMultiplier(dto.getPriceMultiplier());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setDescription(dto.getDescription());
        existing.setRoomTypeIds(dto.getRoomTypeIds());
        return PricingRuleMapper.toDto(repository.save(existing));
    }

    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Seasonal price not found: " + id);
        }
        repository.deleteById(id);
    }
}

