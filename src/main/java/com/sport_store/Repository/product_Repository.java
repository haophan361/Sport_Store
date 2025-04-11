package com.sport_store.Repository;

import com.sport_store.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface product_Repository extends JpaRepository<Products, String>, JpaSpecificationExecutor<Products> {

}
