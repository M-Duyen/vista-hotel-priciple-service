package com.hotelvista.controller;

import com.hotelvista.dto.SeasonalPriceDTO;
import com.hotelvista.service.SeasonalPriceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/seasonal-prices")
public class SeasonalPriceController {
    private final SeasonalPriceService service;

    public SeasonalPriceController(SeasonalPriceService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('pricing_manage')")
    public List<SeasonalPriceDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public SeasonalPriceDTO findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @GetMapping("/room-type/{roomTypeId}")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public List<SeasonalPriceDTO> findApplicable(@PathVariable String roomTypeId,
                                                 @RequestParam(required = false) LocalDate date) {
        return service.findApplicableByRoomTypeIdAndDate(roomTypeId, date == null ? LocalDate.now() : date);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('pricing_manage')")
    public ResponseEntity<SeasonalPriceDTO> save(@Valid @RequestBody SeasonalPriceDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public SeasonalPriceDTO update(@PathVariable Integer id, @Valid @RequestBody SeasonalPriceDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

