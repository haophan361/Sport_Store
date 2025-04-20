package com.sport_store.Service;

import com.sport_store.Entity.Brands;
import com.sport_store.Repository.brand_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class brand_Service {
    private final brand_Repository brand_repository;

    public void saveBrand(String brand) {
        Brands brands = Brands
                .builder()
                .brand_name(brand)
                .build();
        brand_repository.save(brands);
    }

    public void deleteBrand(int brand_id) {
        brand_repository.deleteById(brand_id);
    }

    public Brands getBrand(int brand_id) {
        return brand_repository.findById(brand_id).orElse(null);

    }


    public List<Brands> getAllBrand() {
        return brand_repository.findAll();
    }
}
