package com.sport_store.Repository;

import com.sport_store.Entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface product_Repository extends JpaRepository<Products, String>, JpaSpecificationExecutor<Products> {
    @Query("SELECT p FROM Products p WHERE  LOWER(p.product_name)  LIKE LOWER(CONCAT('%',:keyword, '%'))")
    Page<Products> getProducts_ByName(@Param("keyword") String keyword, Pageable pageable);
}
