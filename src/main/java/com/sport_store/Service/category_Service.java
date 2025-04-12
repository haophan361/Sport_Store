package com.sport_store.Service;

import com.sport_store.Entity.Categories;
import com.sport_store.Repository.category_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class category_Service {
    private final category_Repository category_repository;
    private final image_Service image_service;

    public void saveCategory(String category_name, MultipartFile file) {
        String image_category = image_service.upload(file);
        Categories categories = Categories
                .builder()
                .category_name(category_name)
                .category_image(image_category)
                .build();
        category_repository.save(categories);
    }

    public void deleteCategory(String category_id) {
        category_repository.deleteById(category_id);
    }

    public List<Categories> getAllCategories() {
        return category_repository.findAll();
    }

    public Categories getCategory(String category_id) {
        return category_repository.findById(category_id).orElse(null);
    }
}
