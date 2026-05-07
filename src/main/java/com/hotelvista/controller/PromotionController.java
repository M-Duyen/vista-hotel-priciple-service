package com.hotelvista.controller;

import com.hotelvista.dto.PromotionDTO;
import com.hotelvista.service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {
    private final PromotionService service;

    public PromotionController(PromotionService service) {
        this.service = service;
    }

    @GetMapping
    public List<PromotionDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/active")
    public List<PromotionDTO> findAllActive() {
        return service.findAllActive();
    }

    @GetMapping("/{id}")
    public PromotionDTO findById(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping("/type/{promotionTypeId}")
    public List<PromotionDTO> findByPromotionTypeId(@PathVariable String promotionTypeId) {
        return service.findByPromotionTypeId(promotionTypeId);
    }

    @PostMapping
    public ResponseEntity<PromotionDTO> save(@Valid @RequestBody PromotionDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public PromotionDTO update(@PathVariable String id, @Valid @RequestBody PromotionDTO dto) {
        return service.update(id, dto);
    }

    @PatchMapping("/{id}/status")
    public PromotionDTO updateStatus(@PathVariable String id, @RequestParam boolean active) {
        return service.updateStatus(id, active);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

