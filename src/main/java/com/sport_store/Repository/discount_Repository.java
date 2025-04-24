package com.sport_store.Repository;

import com.sport_store.Entity.Discounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface discount_Repository extends JpaRepository<Discounts, Integer> {
    @Query("SELECT d FROM Discounts d WHERE d.is_active AND (:now BETWEEN d.discount_start_date AND d.discount_end_date)")
    List<Discounts> findAllActiveDiscount(@Param("now") LocalDateTime now);
}
