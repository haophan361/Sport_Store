package com.sport_store.Repository;

import com.sport_store.Entity.Bills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface bill_Repository extends JpaRepository<Bills, String>, JpaSpecificationExecutor<Bills> {
    @Query("SELECT b FROM Bills b WHERE b.bill_purchase_date >= :start AND b.bill_purchase_date <= :end")
    List<Bills> findByBillPurchaseDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT b FROM Bills b")
    List<Bills> findAllBillPurchase();

    @Query("SELECT b FROM Bills b WHERE b.receivers.customers.customer_id = :customerId")
    List<Bills> findByReceivers_Customers_Customer_id(@Param("customerId") String customerId);
}
