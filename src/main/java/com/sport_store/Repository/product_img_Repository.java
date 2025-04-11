package com.sport_store.Repository;

import com.sport_store.Entity.PK.Product_Img_PK;
import com.sport_store.Entity.Product_Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface product_img_Repository extends JpaRepository<Product_Img, Product_Img_PK> {
}
