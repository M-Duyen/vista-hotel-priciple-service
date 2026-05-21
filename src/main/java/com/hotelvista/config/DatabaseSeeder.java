package com.hotelvista.config;

import com.hotelvista.model.CheckInCheckOutPolicy;
import com.hotelvista.model.CheckInCheckOutPolicyRule;
import com.hotelvista.model.HourlyRatePolicy;
import com.hotelvista.model.Promotion;
import com.hotelvista.model.PromotionType;
import com.hotelvista.model.RoomTypePromotion;
import com.hotelvista.model.SeasonalPrice;
import com.hotelvista.model.enums.DiscountType;
import com.hotelvista.model.enums.RuleType;
import com.hotelvista.repository.CheckInCheckOutPolicyRepository;
import com.hotelvista.repository.CheckInCheckOutPolicyRuleRepository;
import com.hotelvista.repository.HourlyRatePolicyRepository;
import com.hotelvista.repository.PromotionRepository;
import com.hotelvista.repository.PromotionTypeRepository;
import com.hotelvista.repository.RoomTypePromotionRepository;
import com.hotelvista.repository.SeasonalPriceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final PromotionTypeRepository promotionTypeRepository;
    private final PromotionRepository promotionRepository;
    private final RoomTypePromotionRepository roomTypePromotionRepository;
    private final SeasonalPriceRepository seasonalPriceRepository;
    private final HourlyRatePolicyRepository hourlyRatePolicyRepository;
    private final CheckInCheckOutPolicyRepository checkInCheckOutPolicyRepository;
    private final CheckInCheckOutPolicyRuleRepository checkInCheckOutPolicyRuleRepository;

    public DatabaseSeeder(PromotionTypeRepository promotionTypeRepository,
                          PromotionRepository promotionRepository,
                          RoomTypePromotionRepository roomTypePromotionRepository,
                          SeasonalPriceRepository seasonalPriceRepository,
                          HourlyRatePolicyRepository hourlyRatePolicyRepository,
                          CheckInCheckOutPolicyRepository checkInCheckOutPolicyRepository,
                          CheckInCheckOutPolicyRuleRepository checkInCheckOutPolicyRuleRepository) {
        this.promotionTypeRepository = promotionTypeRepository;
        this.promotionRepository = promotionRepository;
        this.roomTypePromotionRepository = roomTypePromotionRepository;
        this.seasonalPriceRepository = seasonalPriceRepository;
        this.hourlyRatePolicyRepository = hourlyRatePolicyRepository;
        this.checkInCheckOutPolicyRepository = checkInCheckOutPolicyRepository;
        this.checkInCheckOutPolicyRuleRepository = checkInCheckOutPolicyRuleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedPromotionTypes();
        seedPromotions();
        seedRoomTypePromotions();
        seedSeasonalPrices();
        seedHourlyRatePolicy();
        seedCheckInCheckOutPolicy();
    }

    private void seedPromotionTypes() {
        savePromotionTypeIfMissing("GENERAL_PROMOTION", "Khuyến mãi chung", "Chương trình khuyến mãi áp dụng theo từng chiến dịch.");
        savePromotionTypeIfMissing("SEASONAL_PROMOTION", "Khuyến mãi theo mùa", "Khuyến mãi áp dụng cho mùa cao điểm, thấp điểm hoặc dịp lễ tết.");
        savePromotionTypeIfMissing("FIRST_BOOKING_PROMOTION", "Ưu đãi khách đặt lần đầu", "Giảm giá dành cho khách hàng mới đặt phòng lần đầu.");
        savePromotionTypeIfMissing("LOYALTY_PROMOTION", "Ưu đãi khách hàng thân thiết", "Ưu đãi dành cho khách hàng theo hạng thành viên.");
        savePromotionTypeIfMissing("SPECIAL_EVENT_PROMOTION", "Khuyến mãi sự kiện đặc biệt", "Ưu đãi cho dịp lễ, tết, sự kiện riêng.");
    }

    private void seedPromotions() {
        savePromotionIfMissing(new Promotion(
                "SUMMER-2026",
                "Khuyến mãi mùa hè 2026",
                "Giảm giá theo mùa hè cho các phòng áp dụng trong giai đoạn cao điểm du lịch.",
                DiscountType.PERCENT,
                true,
                "admin",
                "SEASONAL_PROMOTION"
        ));
        savePromotionIfMissing(new Promotion(
                "TET-2026",
                "Ưu đãi Tết 2026",
                "Ưu đãi cố định cho dịp Tết, có thể áp dụng cho nhiều loại phòng.",
                DiscountType.FIXED,
                true,
                "admin",
                "SPECIAL_EVENT_PROMOTION"
        ));
        savePromotionIfMissing(new Promotion(
                "FIRST-BOOKING-2026",
                "Ưu đãi đặt phòng lần đầu",
                "Giảm giá dành cho khách đặt phòng lần đầu trên hệ thống.",
                DiscountType.PERCENT,
                true,
                "admin",
                "FIRST_BOOKING_PROMOTION"
        ));
        savePromotionIfMissing(new Promotion(
                "LOYALTY-GOLD-2026",
                "Ưu đãi Gold Member",
                "Ưu đãi dành cho khách hàng thân thiết hạng Gold trở lên.",
                DiscountType.PERCENT,
                true,
                "admin",
                "LOYALTY_PROMOTION"
        ));
        savePromotionIfMissing(new Promotion(
                "WEEKEND-SPECIAL-2026",
                "Ưu đãi cuối tuần",
                "Ưu đãi chung áp dụng cho một số phòng trong thời gian cuối tuần.",
                DiscountType.PERCENT,
                true,
                "admin",
                "GENERAL_PROMOTION"
        ));
    }

    private void seedRoomTypePromotions() {
        saveRoomTypePromotionIfMissing("STANDARD", "SUMMER-2026", 10.0,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 8, 31));
        saveRoomTypePromotionIfMissing("DELUXE", "TET-2026", 200_000.0,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 2, 28));
        saveRoomTypePromotionIfMissing("SUITE", "FIRST-BOOKING-2026", 5.0,
                LocalDate.of(2026, 1, 1), LocalDate.of(2027, 12, 31));
        saveRoomTypePromotionIfMissing("FAMILY", "LOYALTY-GOLD-2026", 8.0,
                LocalDate.of(2026, 1, 1), LocalDate.of(2027, 12, 31));
        saveRoomTypePromotionIfMissing("STANDARD", "WEEKEND-SPECIAL-2026", 7.0,
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 12, 31));
    }

    private void seedSeasonalPrices() {
        saveSeasonalPriceIfMissing("Summer Peak 2026", 1.15,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 8, 31),
                "Mùa cao điểm du lịch hè",
                Set.of("STANDARD", "DELUXE", "SUITE", "FAMILY"));

        saveSeasonalPriceIfMissing("Holiday Season 2026", 1.25,
                LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 31),
                "Dịp lễ, tết và cuối năm",
                Set.of("STANDARD", "DELUXE", "SUITE"));

        saveSeasonalPriceIfMissing("Low Season 2026", 0.90,
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 5, 31),
                "Mùa thấp điểm để kích cầu đặt phòng",
                Set.of("STANDARD", "DELUXE"));
    }

    private void seedHourlyRatePolicy() {
        if (hourlyRatePolicyRepository.findAll().stream().noneMatch(policy -> "Default hourly rate policy".equalsIgnoreCase(policy.getPolicyName()))) {
            HourlyRatePolicy policy = new HourlyRatePolicy();
            policy.setPolicyName("Default hourly rate policy");
            policy.setWeekendSurcharge(15.0);
            policy.setWeekendDays(new HashSet<>(Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)));
            Map<Integer, Double> baseRates = new HashMap<>();
            baseRates.put(1, 15.0);
            baseRates.put(2, 25.0);
            baseRates.put(3, 35.0);
            baseRates.put(4, 45.0);
            baseRates.put(5, 55.0);
            baseRates.put(6, 65.0);
            baseRates.put(7, 75.0);
            baseRates.put(8, 85.0);
            policy.setBaseRates(baseRates);
            hourlyRatePolicyRepository.save(policy);
        }
    }

    private void seedCheckInCheckOutPolicy() {
        CheckInCheckOutPolicy policy = checkInCheckOutPolicyRepository.findAll().stream()
                .filter(item -> "Standard check-in/check-out policy".equalsIgnoreCase(item.getName()))
                .findFirst()
                .orElseGet(() -> {
                    CheckInCheckOutPolicy created = new CheckInCheckOutPolicy();
                    created.setName("Standard check-in/check-out policy");
                    created.setStandardCheckInTime(LocalTime.of(14, 0));
                    created.setStandardCheckOutTime(LocalTime.of(12, 0));
                    return checkInCheckOutPolicyRepository.save(created);
                });

        seedCheckInOutRulesIfMissing(policy.getId());
    }

    private void seedCheckInOutRulesIfMissing(Long policyId) {
        saveRuleIfMissing(new CheckInCheckOutPolicyRule(
                null,
                RuleType.EARLY_CHECKIN,
                LocalTime.of(5, 0),
                LocalTime.of(9, 0),
                50.0,
                Boolean.FALSE,
                null,
                policyId
        ));

        saveRuleIfMissing(new CheckInCheckOutPolicyRule(
                null,
                RuleType.EARLY_CHECKIN,
                LocalTime.of(9, 0),
                LocalTime.of(14, 0),
                30.0,
                Boolean.FALSE,
                null,
                policyId
        ));

        saveRuleIfMissing(new CheckInCheckOutPolicyRule(
                null,
                RuleType.LATE_CHECKOUT,
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                10.0,
                Boolean.FALSE,
                "gold",
                policyId
        ));

        saveRuleIfMissing(new CheckInCheckOutPolicyRule(
                null,
                RuleType.LATE_CHECKOUT,
                LocalTime.of(13, 0),
                LocalTime.of(15, 0),
                30.0,
                Boolean.FALSE,
                null,
                policyId
        ));

        saveRuleIfMissing(new CheckInCheckOutPolicyRule(
                null,
                RuleType.LATE_CHECKOUT,
                LocalTime.of(15, 0),
                LocalTime.of(18, 0),
                50.0,
                Boolean.FALSE,
                null,
                policyId
        ));

        saveRuleIfMissing(new CheckInCheckOutPolicyRule(
                null,
                RuleType.LATE_CHECKOUT,
                LocalTime.of(18, 0),
                LocalTime.of(23, 59),
                100.0,
                Boolean.TRUE,
                null,
                policyId
        ));
    }

    private void savePromotionTypeIfMissing(String id, String name, String description) {
        if (!promotionTypeRepository.existsById(id)) {
            promotionTypeRepository.save(new PromotionType(id, name, description));
        }
    }

    private void savePromotionIfMissing(Promotion promotion) {
        if (!promotionRepository.existsById(promotion.getPromotionId())) {
            promotionRepository.save(promotion);
        }
    }

    private void saveRoomTypePromotionIfMissing(String roomTypeId,
                                                String promotionId,
                                                double discountValue,
                                                LocalDate startDate,
                                                LocalDate endDate) {
        boolean exists = roomTypePromotionRepository.findAll().stream().anyMatch(existing ->
                Objects.equals(existing.getRoomTypeId(), roomTypeId)
                        && Objects.equals(existing.getPromotionId(), promotionId)
                        && Objects.equals(existing.getDiscountValue(), discountValue)
                        && Objects.equals(existing.getStartDate(), startDate)
                        && Objects.equals(existing.getEndDate(), endDate));

        if (!exists) {
            RoomTypePromotion entity = new RoomTypePromotion();
            entity.setRoomTypeId(roomTypeId);
            entity.setPromotionId(promotionId);
            entity.setDiscountValue(discountValue);
            entity.setStartDate(startDate);
            entity.setEndDate(endDate);
            roomTypePromotionRepository.save(entity);
        }
    }

    private void saveSeasonalPriceIfMissing(String seasonName,
                                            double priceMultiplier,
                                            LocalDate startDate,
                                            LocalDate endDate,
                                            String description,
                                            Set<String> roomTypeIds) {
        boolean exists = seasonalPriceRepository.findAll().stream().anyMatch(existing ->
                Objects.equals(existing.getSeasonName(), seasonName)
                        && Double.compare(existing.getPriceMultiplier(), priceMultiplier) == 0
                        && Objects.equals(existing.getStartDate(), startDate)
                        && Objects.equals(existing.getEndDate(), endDate)
                        && Objects.equals(existing.getDescription(), description)
                        && Objects.equals(new HashSet<>(existing.getRoomTypeIds()), new HashSet<>(roomTypeIds)));

        if (!exists) {
            SeasonalPrice entity = new SeasonalPrice();
            entity.setSeasonName(seasonName);
            entity.setPriceMultiplier(priceMultiplier);
            entity.setStartDate(startDate);
            entity.setEndDate(endDate);
            entity.setDescription(description);
            entity.setRoomTypeIds(new HashSet<>(roomTypeIds));
            seasonalPriceRepository.save(entity);
        }
    }

    private void saveRuleIfMissing(CheckInCheckOutPolicyRule rule) {
        boolean exists = checkInCheckOutPolicyRuleRepository.findAll().stream().anyMatch(existing ->
                Objects.equals(existing.getPolicyId(), rule.getPolicyId())
                        && Objects.equals(existing.getType(), rule.getType())
                        && Objects.equals(existing.getStartTime(), rule.getStartTime())
                        && Objects.equals(existing.getEndTime(), rule.getEndTime())
                        && Objects.equals(existing.getSurchargePercentage(), rule.getSurchargePercentage())
                        && Objects.equals(existing.getDayCharge(), rule.getDayCharge())
                        && Objects.equals(existing.getFreeForMinRankLevel(), rule.getFreeForMinRankLevel()));

        if (!exists) {
            checkInCheckOutPolicyRuleRepository.save(rule);
        }
    }
}


