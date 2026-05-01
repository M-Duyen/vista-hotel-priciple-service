package com.hotelvista.service;

import com.hotelvista.dto.HourlyRateCalculationDTO;
import com.hotelvista.dto.HourlyRateCalculationRequestDTO;
import com.hotelvista.dto.RoomPriceCalculationRequestDTO;
import com.hotelvista.dto.RoomPriceCalculationResponseDTO;
import com.hotelvista.mapper.PricingRuleMapper;
import com.hotelvista.model.HourlyRatePolicy;
import com.hotelvista.model.Promotion;
import com.hotelvista.model.RoomTypePromotion;
import com.hotelvista.model.SeasonalPrice;
import com.hotelvista.model.enums.DiscountType;
import com.hotelvista.util.PricingMathUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PricingCalculationService {
    private static final Map<Integer, Integer> DEFAULT_HOURLY_RATE_TABLE = new HashMap<>() {{
        put(1, 15);
        put(2, 25);
        put(3, 35);
        put(4, 45);
        put(5, 55);
        put(6, 65);
        put(7, 75);
        put(8, 85);
    }};

    private static final Set<DayOfWeek> DEFAULT_WEEKEND_DAYS = Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    private final SeasonalPriceService seasonalPriceService;
    private final RoomTypePromotionService roomTypePromotionService;
    private final PromotionService promotionService;
    private final HourlyRatePolicyService hourlyRatePolicyService;

    public PricingCalculationService(SeasonalPriceService seasonalPriceService,
                                     RoomTypePromotionService roomTypePromotionService,
                                     PromotionService promotionService,
                                     HourlyRatePolicyService hourlyRatePolicyService) {
        this.seasonalPriceService = seasonalPriceService;
        this.roomTypePromotionService = roomTypePromotionService;
        this.promotionService = promotionService;
        this.hourlyRatePolicyService = hourlyRatePolicyService;
    }

    @Transactional(readOnly = true)
    public RoomPriceCalculationResponseDTO calculateRoomPrice(RoomPriceCalculationRequestDTO request) {
        List<String> breakdown = new ArrayList<>();
        double currentPrice = request.getBasePrice();
        double seasonalMultiplier = 1.0;
        Integer seasonalPriceId = null;

        List<SeasonalPrice> seasonalPrices = seasonalPriceService.findApplicableEntitiesByRoomTypeIdAndDate(
                request.getRoomTypeId(), request.getBookingDate());
        if (!seasonalPrices.isEmpty()) {
            SeasonalPrice bestSeasonalPrice = seasonalPrices.stream()
                    .max(Comparator.comparingDouble(SeasonalPrice::getPriceMultiplier)
                            .thenComparing(SeasonalPrice::getStartDate))
                    .orElse(seasonalPrices.getFirst());
            seasonalPriceId = bestSeasonalPrice.getId();
            seasonalMultiplier = bestSeasonalPrice.getPriceMultiplier();
            currentPrice = request.getBasePrice() * seasonalMultiplier;
            breakdown.add(String.format("Seasonal multiplier %.2f applied from %s", seasonalMultiplier, bestSeasonalPrice.getSeasonName()));
        } else {
            breakdown.add("No seasonal price applied");
        }

        List<RoomTypePromotion> promotions = roomTypePromotionService.findApplicableEntitiesByRoomTypeIdAndDate(
                request.getRoomTypeId(), request.getBookingDate());
        List<String> promotionIds = promotions.stream().map(RoomTypePromotion::getPromotionId).toList();
        Map<String, Promotion> activePromotions = promotionService.findAllActive().stream()
                .map(dto -> new Promotion(dto.getPromotionId(), dto.getPromotionName(), dto.getDescription(), dto.getDiscountType(), dto.isActive(), dto.getAdminId(), dto.getPromotionTypeId()))
                .filter(promotion -> promotionIds.contains(promotion.getPromotionId()))
                .collect(Collectors.toMap(Promotion::getPromotionId, p -> p, (left, right) -> left));

        double totalDiscount = 0.0;
        for (RoomTypePromotion link : promotions) {
            Promotion promotion = activePromotions.get(link.getPromotionId());
            if (promotion == null) {
                continue;
            }
            double before = currentPrice;
            currentPrice = PricingMathUtil.applyDiscount(currentPrice, promotion.getDiscountType(), link.getDiscountValue());
            totalDiscount += Math.max(0.0, before - currentPrice);
            breakdown.add(String.format("Promotion %s (%s %.2f) applied", promotion.getPromotionId(), promotion.getDiscountType(), link.getDiscountValue()));
        }

        RoomPriceCalculationResponseDTO response = new RoomPriceCalculationResponseDTO();
        response.setRoomTypeId(request.getRoomTypeId());
        response.setBasePrice(request.getBasePrice());
        response.setSeasonalPriceId(seasonalPriceId);
        response.setSeasonalMultiplier(seasonalMultiplier);
        response.setSeasonalPrice(request.getBasePrice() * seasonalMultiplier);
        response.setAppliedPromotionIds(promotionIds.stream().filter(activePromotions::containsKey).toList());
        response.setTotalDiscountAmount(totalDiscount);
        response.setFinalPrice(PricingMathUtil.clampToZero(currentPrice));
        response.setBreakdown(breakdown);
        return response;
    }

    @Transactional(readOnly = true)
    public HourlyRateCalculationDTO calculateHourlyRate(HourlyRateCalculationRequestDTO request) {
        HourlyRatePolicy policy = Optional.ofNullable(request.getPolicyId())
                .flatMap(hourlyRatePolicyService::findByIdOptional)
                .orElseGet(() -> hourlyRatePolicyService.findAll().stream().findFirst().map(PricingRuleMapper::toEntity)
                        .orElseGet(() -> {
                            HourlyRatePolicy fallback = new HourlyRatePolicy();
                            fallback.setWeekendDays(new java.util.HashSet<>(DEFAULT_WEEKEND_DAYS));
                            fallback.setWeekendSurcharge(15.0);
                            fallback.setBaseRates(new HashMap<>(DEFAULT_HOURLY_RATE_TABLE.entrySet().stream()
                                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().doubleValue()))));
                            return fallback;
                        }));

        return calculateHourlyRate(request.getRoomTypeId(), request.getBasePrice(), request.getHours(), request.getCheckInDateTime(), policy);
    }

    @Transactional(readOnly = true)
    public HourlyRateCalculationDTO calculateHourlyRate(String roomTypeId,
                                                        double basePrice,
                                                        int hours,
                                                        LocalDateTime checkInDateTime,
                                                        HourlyRatePolicy policy) {
        List<String> breakdown = new ArrayList<>();
        int basePercentage = resolveHourlyPercentage(hours, policy);
        breakdown.add(String.format("Số giờ: %d giờ → %d%% giá cơ bản", hours, basePercentage));

        Set<DayOfWeek> weekendDays = policy.getWeekendDays() == null || policy.getWeekendDays().isEmpty()
                ? DEFAULT_WEEKEND_DAYS
                : policy.getWeekendDays();
        boolean weekend = weekendDays.contains(checkInDateTime.getDayOfWeek());
        double weekendSurcharge = weekend ? Optional.ofNullable(policy.getWeekendSurcharge()).orElse(0.0) : 0.0;
        if (weekend) {
            breakdown.add(String.format("Phụ thu cuối tuần: +%.2f%%", weekendSurcharge));
        }

        double totalPercentage = basePercentage + weekendSurcharge;
        double totalAmount = (basePrice * totalPercentage) / 100.0;
        breakdown.add(String.format("Tổng phần trăm: %.2f%%", totalPercentage));
        breakdown.add(String.format("Tổng tiền: %.0f VNĐ", totalAmount));

        HourlyRateCalculationDTO response = new HourlyRateCalculationDTO();
        response.setRoomTypeId(roomTypeId);
        response.setBasePrice(basePrice);
        response.setHours(hours);
        response.setBasePercentage(basePercentage);
        response.setWeekend(weekend);
        response.setWeekendSurcharge(weekendSurcharge);
        response.setTotalPercentage(totalPercentage);
        response.setTotalAmount(totalAmount);
        response.setBreakdown(breakdown);
        return response;
    }

    private int resolveHourlyPercentage(int hours, HourlyRatePolicy policy) {
        Map<Integer, Double> table = policy.getBaseRates() == null || policy.getBaseRates().isEmpty()
                ? DEFAULT_HOURLY_RATE_TABLE.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().doubleValue()))
                : policy.getBaseRates();

        if (hours < 1) {
            return 0;
        }
        if (hours >= 9) {
            return 100;
        }
        return table.entrySet().stream()
                .filter(entry -> entry.getKey() <= hours)
                .max(Map.Entry.comparingByKey())
                .map(entry -> entry.getValue().intValue())
                .orElse(100);
    }
}

