package com.sport_store.Repository;

import com.sport_store.Entity.Colors;
import com.sport_store.Entity.PK.Product_Img_PK;
import com.sport_store.Entity.Product_Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface product_img_Repository extends JpaRepository<Product_Img, Product_Img_PK> {
    @Query("SELECT pi.colors FROM Product_Img pi WHERE pi.products.product_id=:product_id")
    List<Colors> getAllColorByProduct(String product_id);
}
