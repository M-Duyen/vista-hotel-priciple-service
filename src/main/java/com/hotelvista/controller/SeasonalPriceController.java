package com.hotelvista.controller;

import com.hotelvista.dto.SeasonalPriceDTO;
import com.hotelvista.service.SeasonalPriceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/seasonal-prices")
public class SeasonalPriceController {
    private final SeasonalPriceService service;

    public SeasonalPriceController(SeasonalPriceService service) {
        this.service = service;
    }

    @GetMapping
    public List<SeasonalPriceDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SeasonalPriceDTO findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @GetMapping("/room-type/{roomTypeId}")
    public List<SeasonalPriceDTO> findApplicable(@PathVariable String roomTypeId,
                                                 @RequestParam(required = false) LocalDate date) {
        return service.findApplicableByRoomTypeIdAndDate(roomTypeId, date == null ? LocalDate.now() : date);
    }

    @PostMapping
    public ResponseEntity<SeasonalPriceDTO> save(@Valid @RequestBody SeasonalPriceDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public SeasonalPriceDTO update(@PathVariable Integer id, @Valid @RequestBody SeasonalPriceDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

