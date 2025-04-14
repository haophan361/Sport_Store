package com.sport_store.Repository;

import com.sport_store.Entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface image_Repository extends JpaRepository<Images, Integer> {
}
