package com.hotelvista.controller;

import com.hotelvista.dto.RoomTypePromotionDTO;
import com.hotelvista.service.RoomTypePromotionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/room-type-promotions")
public class RoomTypePromotionController {
    private final RoomTypePromotionService service;

    public RoomTypePromotionController(RoomTypePromotionService service) {
        this.service = service;
    }

    @GetMapping
    public List<RoomTypePromotionDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public RoomTypePromotionDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/room-type/{roomTypeId}")
    public List<RoomTypePromotionDTO> findByRoomTypeId(@PathVariable String roomTypeId) {
        return service.findByRoomTypeId(roomTypeId);
    }

    @GetMapping("/applicable/{roomTypeId}")
    public List<RoomTypePromotionDTO> findApplicable(@PathVariable String roomTypeId,
                                                     @RequestParam(required = false) LocalDate date) {
        return service.findApplicableByRoomTypeIdAndDate(roomTypeId, date == null ? LocalDate.now() : date);
    }

    @PostMapping
    public ResponseEntity<RoomTypePromotionDTO> save(@Valid @RequestBody RoomTypePromotionDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public RoomTypePromotionDTO update(@PathVariable Long id, @Valid @RequestBody RoomTypePromotionDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

