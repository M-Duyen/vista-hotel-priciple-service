package com.hotelvista.scheduler;

import com.hotelvista.model.Promotion;
import com.hotelvista.model.RoomTypePromotion;
import com.hotelvista.repository.PromotionRepository;
import com.hotelvista.repository.RoomTypePromotionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class PromotionScheduler {
    private final PromotionRepository promotionRepository;
    private final RoomTypePromotionRepository roomTypePromotionRepository;

    public PromotionScheduler(PromotionRepository promotionRepository,
                              RoomTypePromotionRepository roomTypePromotionRepository) {
        this.promotionRepository = promotionRepository;
        this.roomTypePromotionRepository = roomTypePromotionRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deactivateExpiredPromotionsDaily() {
        deactivateExpiredPromotionsInternal();
    }

    @Scheduled(fixedRate = 3_600_000)
    @Transactional
    public void hourlyPromotionCheck() {
        deactivateExpiredPromotionsInternal();
    }

    private void deactivateExpiredPromotionsInternal() {
        LocalDate today = LocalDate.now();
        List<Promotion> activePromotions = promotionRepository.findByActiveTrue();

        for (Promotion promotion : activePromotions) {
            List<RoomTypePromotion> links = roomTypePromotionRepository.findByPromotionId(promotion.getPromotionId());
            boolean allExpired = !links.isEmpty() && links.stream().allMatch(link -> link.getEndDate().isBefore(today));
            if (allExpired) {
                promotion.setActive(false);
                promotionRepository.save(promotion);
            }
        }
    }
}


