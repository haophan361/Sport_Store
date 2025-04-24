package com.sport_store.Repository;

import com.sport_store.Entity.Bill_Details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface bill_details_Repository extends JpaRepository<Bill_Details, String> {
    @Query("SELECT bd FROM Bill_Details bd, Bills b WHERE b.bill_purchase_date >= :start AND b.bill_purchase_date <= :end")
    List<Bill_Details> findByBillsBillPurchaseDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT bd FROM Bill_Details bd")
    List<Bill_Details> findAllBillsBillPurchase();
}

