package com.hotelvista.controller;

import com.hotelvista.dto.PromotionTypeDTO;
import com.hotelvista.service.PromotionTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotion-types")
public class PromotionTypeController {
    private final PromotionTypeService service;

    public PromotionTypeController(PromotionTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<PromotionTypeDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PromotionTypeDTO findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('promotion_type_manage')")
    public ResponseEntity<PromotionTypeDTO> save(@Valid @RequestBody PromotionTypeDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('promotion_type_manage')")
    public PromotionTypeDTO update(@PathVariable String id, @Valid @RequestBody PromotionTypeDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('promotion_type_manage')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

