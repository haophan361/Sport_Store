package com.sport_store.Controller.api;

import com.sport_store.Entity.Colors;
import com.sport_store.Service.color_Service;
import com.sport_store.Service.product_img_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class color_API {
    private final color_Service color_service;
    private final product_img_Service product_img_service;

    @PostMapping("/admin/insert_color")
    public ResponseEntity<?> insert_Color(@RequestBody String color) {
        color_service.saveColor(color);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm màu thành công"));
    }

    @GetMapping("/getAllColor")
    public List<Colors> getAllColor(@RequestParam String product_id) {
        return product_img_service.getAllColorByProduct(product_id);
    }


}
