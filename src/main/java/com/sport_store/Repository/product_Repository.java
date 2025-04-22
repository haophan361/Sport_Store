package com.sport_store.Repository;

import com.sport_store.Entity.Brands;
import com.sport_store.Entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface product_Repository extends JpaRepository<Products, String>, JpaSpecificationExecutor<Products> {
    @Query("SELECT p FROM Products p WHERE  LOWER(p.product_name)  LIKE LOWER(CONCAT('%',:keyword, '%'))")
    Page<Products> getProducts_ByName(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p.brands FROM Products p WHERE p.categories.category_id= :category_id")
    List<Brands> getBrandByCategory(int category_id);
}
