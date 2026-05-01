package com.hotelvista.service;

import com.hotelvista.dto.PromotionTypeDTO;
import com.hotelvista.exception.ResourceNotFoundException;
import com.hotelvista.mapper.PromotionMapper;
import com.hotelvista.model.PromotionType;
import com.hotelvista.repository.PromotionTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PromotionTypeService {
    private final PromotionTypeRepository repository;

    public PromotionTypeService(PromotionTypeRepository repository) {
        this.repository = repository;
    }

    public List<PromotionTypeDTO> findAll() {
        return repository.findAll().stream().map(PromotionMapper::toDto).toList();
    }

    public PromotionTypeDTO findById(String id) {
        return repository.findById(id).map(PromotionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion type not found: " + id));
    }

    @Transactional
    public PromotionTypeDTO save(PromotionTypeDTO dto) {
        PromotionType saved = repository.save(PromotionMapper.toEntity(dto));
        return PromotionMapper.toDto(saved);
    }

    @Transactional
    public PromotionTypeDTO update(String id, PromotionTypeDTO dto) {
        PromotionType existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion type not found: " + id));
        existing.setPromotionTypeName(dto.getPromotionTypeName());
        existing.setDescription(dto.getDescription());
        return PromotionMapper.toDto(repository.save(existing));
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Promotion type not found: " + id);
        }
        repository.deleteById(id);
    }

    public boolean exists(String id) {
        return repository.existsById(id);
    }
}

