package com.sport_store.Repository;

import com.sport_store.Entity.Bills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface bill_Repository extends JpaRepository<Bills, String> {
    @Query("SELECT b FROM Bills b WHERE b.bill_purchase_date >= :start AND b.bill_purchase_date <= :end")
    List<Bills> findByBillPurchaseDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT b FROM Bills b")
    List<Bills> findAllBillPurchase();
}
