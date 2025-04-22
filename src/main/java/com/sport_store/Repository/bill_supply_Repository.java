package com.sport_store.Repository;

import com.sport_store.Entity.Bill_Supplies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface bill_supply_Repository extends JpaRepository<Bill_Supplies, String> {
    @Query("SELECT bs FROM Bill_Supplies bs WHERE bs.bill_supply_date >= :start AND bs.bill_supply_date <= :end")
    List<Bill_Supplies> findByBillSupplyDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT bs FROM Bill_Supplies bs")
    List<Bill_Supplies> findAllBillSupply();
}
