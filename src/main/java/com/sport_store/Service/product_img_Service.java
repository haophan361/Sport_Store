package com.sport_store.Service;

import com.sport_store.Entity.Colors;
import com.sport_store.Entity.Images;
import com.sport_store.Entity.Product_Img;
import com.sport_store.Entity.Products;
import com.sport_store.Repository.product_img_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class product_img_Service {
    private final product_img_Repository product_img_repository;

    public void save_Product_Img(Products product, Colors color, List<Images> images) {
        for (Images img : images) {
            Product_Img product_img = Product_Img
                    .builder()
                    .products(product)
                    .colors(color)
                    .images(img)
                    .build();
            product_img_repository.save(product_img);
        }
    }

    
}
