package com.hotelvista.config;

import com.hotelvista.model.*;
import com.hotelvista.model.enums.RuleType;
import com.hotelvista.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final SeasonalPriceRepository seasonalPriceRepository;
    private final HourlyRatePolicyRepository hourlyRatePolicyRepository;
    private final CheckInCheckOutPolicyRepository checkInCheckOutPolicyRepository;
    private final CheckInCheckOutPolicyRuleRepository checkInCheckOutPolicyRuleRepository;

    public DatabaseSeeder(
                          SeasonalPriceRepository seasonalPriceRepository,
                          HourlyRatePolicyRepository hourlyRatePolicyRepository,
                          CheckInCheckOutPolicyRepository checkInCheckOutPolicyRepository,
                          CheckInCheckOutPolicyRuleRepository checkInCheckOutPolicyRuleRepository) {
        this.seasonalPriceRepository = seasonalPriceRepository;
        this.hourlyRatePolicyRepository = hourlyRatePolicyRepository;
        this.checkInCheckOutPolicyRepository = checkInCheckOutPolicyRepository;
        this.checkInCheckOutPolicyRuleRepository = checkInCheckOutPolicyRuleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedSeasonalPrices();
        seedHourlyRatePolicy();
        seedCheckInCheckOutPolicy();
    }


    private void seedSeasonalPrices() {
        saveSeasonalPriceIfMissing("Summer Peak 2026", 1.15,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 8, 31),
                "Mùa cao điểm du lịch hè",
                Set.of("RT-STD", "RT-DEL", "RT-SUI"));

        saveSeasonalPriceIfMissing("Holiday Season 2026", 1.25,
                LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 31),
                "Dịp lễ, tết và cuối năm",
                Set.of("RT-STD", "RT-DEL", "RT-SUI"));

        saveSeasonalPriceIfMissing("Low Season 2026", 0.90,
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 5, 31),
                "Mùa thấp điểm để kích cầu đặt phòng",
                Set.of("RT-STD", "RT-DEL"));
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


