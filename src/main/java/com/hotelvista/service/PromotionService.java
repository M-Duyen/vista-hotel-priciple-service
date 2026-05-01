package com.hotelvista.service;

import com.hotelvista.dto.PromotionDTO;
import com.hotelvista.exception.ResourceNotFoundException;
import com.hotelvista.mapper.PromotionMapper;
import com.hotelvista.model.Promotion;
import com.hotelvista.repository.PromotionRepository;
import com.hotelvista.repository.PromotionTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionTypeRepository promotionTypeRepository;
    private final RoomTypePromotionService roomTypePromotionService;

    public PromotionService(PromotionRepository promotionRepository,
                            PromotionTypeRepository promotionTypeRepository,
                            RoomTypePromotionService roomTypePromotionService) {
        this.promotionRepository = promotionRepository;
        this.promotionTypeRepository = promotionTypeRepository;
        this.roomTypePromotionService = roomTypePromotionService;
    }

    public List<PromotionDTO> findAll() {
        return promotionRepository.findAll().stream().map(PromotionMapper::toDto).toList();
    }

    public PromotionDTO findById(String id) {
        return promotionRepository.findById(id).map(PromotionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found: " + id));
    }

    public List<PromotionDTO> findAllActive() {
        return promotionRepository.findByActiveTrue().stream().map(PromotionMapper::toDto).toList();
    }

    public List<PromotionDTO> findByPromotionTypeId(String promotionTypeId) {
        return promotionRepository.findByPromotionTypeId(promotionTypeId).stream().map(PromotionMapper::toDto).toList();
    }

    @Transactional
    public PromotionDTO save(PromotionDTO dto) {
        if (!promotionTypeRepository.existsById(dto.getPromotionTypeId())) {
            throw new ResourceNotFoundException("Promotion type not found: " + dto.getPromotionTypeId());
        }
        Promotion saved = promotionRepository.save(PromotionMapper.toEntity(dto));
        return PromotionMapper.toDto(saved);
    }

    @Transactional
    public PromotionDTO update(String id, PromotionDTO dto) {
        Promotion existing = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found: " + id));
        if (dto.getPromotionName() != null) {
            existing.setPromotionName(dto.getPromotionName());
        }
        existing.setDescription(dto.getDescription());
        if (dto.getDiscountType() != null) {
            existing.setDiscountType(dto.getDiscountType());
        }
        existing.setActive(dto.isActive());
        existing.setAdminId(dto.getAdminId());
        if (dto.getPromotionTypeId() != null) {
            if (!promotionTypeRepository.existsById(dto.getPromotionTypeId())) {
                throw new ResourceNotFoundException("Promotion type not found: " + dto.getPromotionTypeId());
            }
            existing.setPromotionTypeId(dto.getPromotionTypeId());
        }
        return PromotionMapper.toDto(promotionRepository.save(existing));
    }

    @Transactional
    public void delete(String id) {
        if (!promotionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Promotion not found: " + id);
        }
        roomTypePromotionService.deleteByPromotionId(id);
        promotionRepository.deleteById(id);
    }

    @Transactional
    public PromotionDTO updateStatus(String id, boolean active) {
        Promotion existing = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found: " + id));
        existing.setActive(active);
        return PromotionMapper.toDto(promotionRepository.save(existing));
    }
}

