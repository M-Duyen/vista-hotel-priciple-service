package com.hotelvista.controller;

import com.hotelvista.dto.HourlyRateCalculationDTO;
import com.hotelvista.dto.HourlyRateCalculationRequestDTO;
import com.hotelvista.dto.HourlyRatePolicyDTO;
import com.hotelvista.dto.RoomPriceCalculationRequestDTO;
import com.hotelvista.dto.RoomPriceCalculationResponseDTO;
import com.hotelvista.model.HourlyRatePolicy;
import com.hotelvista.model.SeasonalPrice;
import com.hotelvista.service.HourlyRatePolicyService;
import com.hotelvista.service.SeasonalPriceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pricing")
public class PricingController {
    private final SeasonalPriceService seasonalPriceService;
    private final HourlyRatePolicyService hourlyRatePolicyService;

    public PricingController(SeasonalPriceService seasonalPriceService,
                             HourlyRatePolicyService hourlyRatePolicyService) {
        this.seasonalPriceService = seasonalPriceService;
        this.hourlyRatePolicyService = hourlyRatePolicyService;
    }

    @PostMapping("/calculate-room-price")
    public ResponseEntity<RoomPriceCalculationResponseDTO> calculateRoomPrice(
            @Valid @RequestBody RoomPriceCalculationRequestDTO request) {
        List<SeasonalPrice> applicablePrices = seasonalPriceService
                .findApplicableEntitiesByRoomTypeIdAndDate(request.getRoomTypeId(), request.getBookingDate());

        SeasonalPrice appliedSeason = applicablePrices.isEmpty() ? null : applicablePrices.get(0);
        double multiplier = appliedSeason == null ? 1.0 : appliedSeason.getPriceMultiplier();
        double seasonalPrice = request.getBasePrice() * multiplier;

        List<String> breakdown = new ArrayList<>();
        breakdown.add(String.format("Base price: %.0f VND", request.getBasePrice()));
        if (appliedSeason != null) {
            breakdown.add(String.format("%s multiplier: %.2f", appliedSeason.getSeasonName(), multiplier));
        } else {
            breakdown.add("No seasonal price applied");
        }
        breakdown.add(String.format("Final price: %.0f VND", seasonalPrice));

        RoomPriceCalculationResponseDTO response = new RoomPriceCalculationResponseDTO();
        response.setRoomTypeId(request.getRoomTypeId());
        response.setBasePrice(request.getBasePrice());
        response.setSeasonalPriceId(appliedSeason == null ? null : appliedSeason.getId());
        response.setSeasonalMultiplier(multiplier);
        response.setSeasonalPrice(seasonalPrice);
        response.setAppliedPromotionIds(List.of());
        response.setTotalDiscountAmount(0.0);
        response.setFinalPrice(seasonalPrice);
        response.setBreakdown(breakdown);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculate-hourly-rate")
    public ResponseEntity<HourlyRateCalculationDTO> calculateHourlyRate(
            @Valid @RequestBody HourlyRateCalculationRequestDTO request) {
        HourlyRatePolicy policy = request.getPolicyId() == null
                ? toPolicy(hourlyRatePolicyService.getDefaultPolicy())
                : hourlyRatePolicyService.findByIdOptional(request.getPolicyId())
                .orElseGet(() -> toPolicy(hourlyRatePolicyService.getDefaultPolicy()));

        Map<Integer, Double> baseRates = policy.getBaseRates();
        Double basePercentageValue = baseRates == null ? null : baseRates.get(request.getHours());
        if (basePercentageValue == null) {
            throw new IllegalArgumentException("No hourly rate configured for " + request.getHours() + " hour(s)");
        }

        DayOfWeek dayOfWeek = request.getCheckInDateTime().getDayOfWeek();
        boolean weekend = policy.getWeekendDays() != null && policy.getWeekendDays().contains(dayOfWeek);
        double weekendSurcharge = weekend ? policy.getWeekendSurcharge() : 0.0;
        double totalPercentage = basePercentageValue + weekendSurcharge;
        double totalAmount = request.getBasePrice() * totalPercentage / 100.0;

        List<String> breakdown = new ArrayList<>();
        breakdown.add(String.format("Base price: %.0f VND", request.getBasePrice()));
        breakdown.add(String.format("%d hour(s): %.0f%%", request.getHours(), basePercentageValue));
        if (weekend) {
            breakdown.add(String.format("Weekend surcharge: %.0f%%", weekendSurcharge));
        }
        breakdown.add(String.format("Total: %.0f%% = %.0f VND", totalPercentage, totalAmount));

        HourlyRateCalculationDTO response = new HourlyRateCalculationDTO();
        response.setRoomTypeId(request.getRoomTypeId());
        response.setBasePrice(request.getBasePrice());
        response.setHours(request.getHours());
        response.setBasePercentage(basePercentageValue.intValue());
        response.setWeekend(weekend);
        response.setIsWeekend(weekend);
        response.setWeekendSurcharge(weekendSurcharge);
        response.setTotalPercentage(totalPercentage);
        response.setTotalAmount(totalAmount);
        response.setBreakdown(breakdown);

        return ResponseEntity.ok(response);
    }

    private HourlyRatePolicy toPolicy(HourlyRatePolicyDTO dto) {
        HourlyRatePolicy policy = new HourlyRatePolicy();
        policy.setId(dto.getId());
        policy.setPolicyName(dto.getPolicyName());
        policy.setWeekendSurcharge(dto.getWeekendSurcharge());
        policy.setWeekendDays(dto.getWeekendDays());
        policy.setBaseRates(dto.getBaseRates());
        return policy;
    }
}
