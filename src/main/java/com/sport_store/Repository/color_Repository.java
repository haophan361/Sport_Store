package com.sport_store.Repository;

import com.sport_store.Entity.Colors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface color_Repository extends JpaRepository<Colors, Integer> {
    
}
