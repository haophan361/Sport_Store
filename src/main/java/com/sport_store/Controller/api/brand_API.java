package com.sport_store.Controller.api;

import com.sport_store.Service.brand_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RequiredArgsConstructor
@RestController
public class brand_API {
    private final brand_Service brand_service;

    @PostMapping("/admin/insert_brand")
    public ResponseEntity<?> insert_brand(@RequestBody String brand_name) {
        brand_service.saveBrand(brand_name);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm thương hiệu mới thành công"));
    }

    @PostMapping("admin/delete_brand")
    public ResponseEntity<?> delete_brand(@RequestBody String brand_id) {
        brand_service.deleteBrand(brand_id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Xóa thương hiệu thành công"));
    }
}
