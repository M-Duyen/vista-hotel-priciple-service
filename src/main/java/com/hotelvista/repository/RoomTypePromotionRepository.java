package com.hotelvista.repository;

import com.hotelvista.model.RoomTypePromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface RoomTypePromotionRepository extends JpaRepository<RoomTypePromotion, Long> {
    List<RoomTypePromotion> findByRoomTypeId(String roomTypeId);

    List<RoomTypePromotion> findByPromotionId(String promotionId);

    @Query("""
            select rtp
            from RoomTypePromotion rtp
            where rtp.roomTypeId = :roomTypeId
              and rtp.startDate <= :date
              and rtp.endDate >= :date
            order by rtp.discountValue desc
            """)
    List<RoomTypePromotion> findApplicableByRoomTypeIdAndDate(@Param("roomTypeId") String roomTypeId,
                                                              @Param("date") LocalDate date);

    @Query("""
            select rtp
            from RoomTypePromotion rtp
            where rtp.promotionId in :promotionIds
              and rtp.startDate <= :date
              and rtp.endDate >= :date
            order by rtp.discountValue desc
            """)
    List<RoomTypePromotion> findApplicableByPromotionIdsAndDate(@Param("promotionIds") Collection<String> promotionIds,
                                                                @Param("date") LocalDate date);

    @Modifying
    @Transactional
    void deleteByPromotionId(String promotionId);
}

