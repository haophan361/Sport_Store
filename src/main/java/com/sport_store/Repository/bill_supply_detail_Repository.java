package com.sport_store.Repository;

import com.sport_store.Entity.Bill_Supply_Details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface bill_supply_detail_Repository extends JpaRepository<Bill_Supply_Details, String> {
    @Query("SELECT b FROM Bill_Supply_Details b WHERE b.bill_supplies.bill_supply_id= :bill_supply_id")
    List<Bill_Supply_Details> getBillSupplyDetails(String bill_supply_id);
}
