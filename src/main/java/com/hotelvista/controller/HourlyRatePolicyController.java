package com.hotelvista.controller;

import com.hotelvista.dto.HourlyRatePolicyDTO;
import com.hotelvista.service.HourlyRatePolicyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hourly-rate-policies")
public class HourlyRatePolicyController {
    private final HourlyRatePolicyService service;

    public HourlyRatePolicyController(HourlyRatePolicyService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('pricing_manage') or hasAuthority('room_view')")
    public List<HourlyRatePolicyDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('pricing_manage') or hasAuthority('room_view')")
    public HourlyRatePolicyDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('pricing_manage')")
    public ResponseEntity<HourlyRatePolicyDTO> save(@Valid @RequestBody HourlyRatePolicyDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public HourlyRatePolicyDTO update(@PathVariable Long id, @Valid @RequestBody HourlyRatePolicyDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

