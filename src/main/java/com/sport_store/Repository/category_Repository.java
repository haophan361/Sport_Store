package com.sport_store.Repository;

import com.sport_store.Entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface category_Repository extends JpaRepository<Categories, Integer> {

}
