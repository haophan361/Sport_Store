package com.sport_store.Repository;

import com.sport_store.Entity.Brands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface brand_Repository extends JpaRepository<Brands, String> {

}
