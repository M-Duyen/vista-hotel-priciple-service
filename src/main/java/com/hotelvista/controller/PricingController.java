package com.hotelvista.controller;

import com.hotelvista.dto.HourlyRateCalculationDTO;
import com.hotelvista.dto.HourlyRateCalculationRequestDTO;
import com.hotelvista.dto.RoomPriceCalculationRequestDTO;
import com.hotelvista.dto.RoomPriceCalculationResponseDTO;
import com.hotelvista.service.PricingCalculationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pricing")
public class PricingController {
    private final PricingCalculationService service;

    public PricingController(PricingCalculationService service) {
        this.service = service;
    }

    @PostMapping("/calculate-room-price")
    public RoomPriceCalculationResponseDTO calculateRoomPrice(@Valid @RequestBody RoomPriceCalculationRequestDTO request) {
        return service.calculateRoomPrice(request);
    }

    @PostMapping("/calculate-hourly-rate")
    public HourlyRateCalculationDTO calculateHourlyRate(@Valid @RequestBody HourlyRateCalculationRequestDTO request) {
        return service.calculateHourlyRate(request);
    }
}

