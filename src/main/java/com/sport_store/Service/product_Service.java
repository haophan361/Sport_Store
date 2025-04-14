package com.sport_store.Service;

import com.sport_store.DTO.request.product_Request.product_request;
import com.sport_store.Entity.Brands;
import com.sport_store.Entity.Categories;
import com.sport_store.Entity.Products;
import com.sport_store.Repository.product_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class product_Service {
    private final product_Repository product_repository;
    private final brand_Service brand_service;
    private final category_Service category_service;

    public void Save_Product(product_request request) {
        Brands brand = brand_service.getBrand(request.getBrand_id());
        Categories category = category_service.getCategory(request.getCategory_id());
        Products product = Products
                .builder()
                .product_id(request.getProduct_id())
                .product_name(request.getProduct_name())
                .product_detail(request.getDescription())
                .brands(brand)
                .categories(category)
                .is_active(request.isActive())
                .build();
        product_repository.save(product);
    }

    public void delete_Product(String product_id) {
        Products product = product_repository.findById(product_id).orElse(null);
        if (product != null) {
            product.set_active(false);
            product_repository.save(product);
        }
    }

    public List<Products> getAllProducts() {
        return product_repository.findAll();
    }

    public Products getProductById(String product_id) {
        return product_repository.findById(product_id).orElse(null);
    }
}
