package com.sport_store.Repository;

import com.sport_store.Entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface category_Repository extends JpaRepository<Categories, String> {
    @Query("SELECT c.category_name FROM Categories c")
    public List<String> getAllCategories();
}
