package com.appliance_store.Repository;

import com.appliance_store.Entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface discount_Repository extends JpaRepository<Discount,String> {

}
