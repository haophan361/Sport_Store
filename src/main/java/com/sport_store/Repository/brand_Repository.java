package com.sport_store.Repository;

import com.sport_store.Entity.Brands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface brand_Repository extends JpaRepository<Brands, Integer> {
    @Query("SELECT b.brand_name FROM Brands b")
    List<String> getAllBrand();
}
