package com.sport_store.Controller.api;

import com.sport_store.Entity.Brands;
import com.sport_store.Service.brand_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class brand_API {
    private final brand_Service brand_service;

    @PostMapping("/admin/insert_brand")
    public ResponseEntity<?> insert_brand(@RequestBody String brand_name) {
        brand_service.saveBrand(brand_name);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm thương hiệu thành công"));
    }

    @DeleteMapping("admin/delete_brand")
    public ResponseEntity<?> delete_brand(@RequestParam int brand_id) throws Exception {
        try {
            brand_service.deleteBrand(brand_id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Xóa thương hiệu thành công"));
        } catch (Exception e) {
            throw new Exception("Không thể xóa thương hiệu do " + e.getMessage());
        }

    }

    @GetMapping("/getAllBrand")
    public List<Brands> getAllBrand() {
        return brand_service.getAllBrand();
    }
}
