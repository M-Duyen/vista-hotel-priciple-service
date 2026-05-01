package com.hotelvista.repository;

import com.hotelvista.model.SeasonalPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SeasonalPriceRepository extends JpaRepository<SeasonalPrice, Integer> {
    @Query("""
            select distinct sp
            from SeasonalPrice sp
            join sp.roomTypeIds roomTypeId
            where roomTypeId = :roomTypeId
              and sp.startDate <= :date
              and sp.endDate >= :date
            order by sp.priceMultiplier desc
            """)
    List<SeasonalPrice> findApplicableByRoomTypeIdAndDate(@Param("roomTypeId") String roomTypeId,
                                                           @Param("date") LocalDate date);
}

