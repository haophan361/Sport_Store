package com.sport_store.Repository;

import com.sport_store.Entity.Bill_Supplies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface bill_supply_Repository extends JpaRepository<Bill_Supplies, String> {

}
