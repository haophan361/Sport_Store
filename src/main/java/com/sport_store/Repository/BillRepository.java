package com.sport_store.Repository;

import com.sport_store.Entity.Bills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bills, String> {
    // Add custom query methods if needed
} 