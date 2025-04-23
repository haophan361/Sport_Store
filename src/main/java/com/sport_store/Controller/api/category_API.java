package com.sport_store.Controller.api;

import com.sport_store.Entity.Categories;
import com.sport_store.Service.category_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class category_API {
    private final category_Service category_service;

    @PostMapping("/admin/insert_category")
    public ResponseEntity<?> insert_category(@RequestPart("category_image") MultipartFile file,
                                             @RequestPart("category_name") String category_name) {
        category_service.saveCategory(category_name, file);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm loại sản phẩm thành công"));
    }

    @PostMapping("/admin/delete_category")
    public ResponseEntity<?> delete_category(@RequestParam int category_id) throws Exception {
        try {
            category_service.deleteCategory(category_id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Xóa loại sản phẩm thành công"));
        } catch (Exception e) {
            throw new Exception("Không thể xóa loại sản phẩm này do " + e.getMessage());
        }
    }

    @GetMapping("/getAllCategory")
    public List<Categories> getAllCategory() {
        return category_service.getAllCategories();
    }
}
