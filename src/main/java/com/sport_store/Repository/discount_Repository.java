package com.sport_store.Repository;

import com.sport_store.Entity.Discounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface discount_Repository extends JpaRepository<Discounts, String> {
}
