package com.sport_store.Repository;

import com.sport_store.Entity.Bills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface bills_Repository extends JpaRepository<Bills, String> {
    @Query("SELECT b FROM Bills b WHERE b.receivers.customers.customer_id = :customerId")
    List<Bills> findByReceivers_Customers_Customer_id(@Param("customerId") String customerId);
} 