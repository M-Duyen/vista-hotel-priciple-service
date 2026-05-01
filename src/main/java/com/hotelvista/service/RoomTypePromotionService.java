package com.hotelvista.service;

import com.hotelvista.dto.RoomTypePromotionDTO;
import com.hotelvista.exception.ResourceNotFoundException;
import com.hotelvista.mapper.PricingRuleMapper;
import com.hotelvista.model.RoomTypePromotion;
import com.hotelvista.repository.PromotionRepository;
import com.hotelvista.repository.RoomTypePromotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
public class RoomTypePromotionService {
    private final RoomTypePromotionRepository repository;
    private final PromotionRepository promotionRepository;

    public RoomTypePromotionService(RoomTypePromotionRepository repository,
                                    PromotionRepository promotionRepository) {
        this.repository = repository;
        this.promotionRepository = promotionRepository;
    }

    public List<RoomTypePromotionDTO> findAll() {
        return repository.findAll().stream().map(PricingRuleMapper::toDto).toList();
    }

    public RoomTypePromotionDTO findById(Long id) {
        return repository.findById(id).map(PricingRuleMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Room type promotion not found: " + id));
    }

    public List<RoomTypePromotionDTO> findByRoomTypeId(String roomTypeId) {
        return repository.findByRoomTypeId(roomTypeId).stream().map(PricingRuleMapper::toDto).toList();
    }

    public List<RoomTypePromotionDTO> findApplicableByRoomTypeIdAndDate(String roomTypeId, LocalDate date) {
        return repository.findApplicableByRoomTypeIdAndDate(roomTypeId, date).stream().map(PricingRuleMapper::toDto).toList();
    }

    public List<RoomTypePromotion> findApplicableEntitiesByRoomTypeIdAndDate(String roomTypeId, LocalDate date) {
        return repository.findApplicableByRoomTypeIdAndDate(roomTypeId, date);
    }

    @Transactional
    public RoomTypePromotionDTO save(RoomTypePromotionDTO dto) {
        if (!promotionRepository.existsById(dto.getPromotionId())) {
            throw new ResourceNotFoundException("Promotion not found: " + dto.getPromotionId());
        }
        RoomTypePromotion saved = repository.save(PricingRuleMapper.toEntity(dto));
        return PricingRuleMapper.toDto(saved);
    }

    @Transactional
    public RoomTypePromotionDTO update(Long id, RoomTypePromotionDTO dto) {
        RoomTypePromotion existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room type promotion not found: " + id));
        existing.setRoomTypeId(dto.getRoomTypeId());
        existing.setPromotionId(dto.getPromotionId());
        existing.setDiscountValue(dto.getDiscountValue());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        return PricingRuleMapper.toDto(repository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Room type promotion not found: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public void deleteByPromotionId(String promotionId) {
        repository.deleteByPromotionId(promotionId);
    }

    public List<RoomTypePromotionDTO> findByPromotionIdsAndDate(Collection<String> promotionIds, LocalDate date) {
        return repository.findApplicableByPromotionIdsAndDate(promotionIds, date).stream().map(PricingRuleMapper::toDto).toList();
    }
}

