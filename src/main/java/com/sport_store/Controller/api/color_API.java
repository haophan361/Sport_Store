package com.sport_store.Controller.api;

import com.sport_store.Entity.Colors;
import com.sport_store.Service.color_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class color_API {
    private final color_Service color_service;

    @PostMapping("/admin/insert_color")
    public ResponseEntity<?> insert_Color(@RequestBody String color) {
        color_service.saveColor(color);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm màu thành công"));
    }

    @GetMapping("/getAllColor")
    public List<Colors> getAllColor() {
        return color_service.getAllColors();
    }

    @DeleteMapping("/admin/delete_color")
    public ResponseEntity<?> delete_Color(@RequestParam int color_id) throws Exception {
        try {
            color_service.deleteColor(color_id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Xóa màu sắc thành công"));
        } catch (Exception e) {
            throw new Exception("Không thể xóa màu do " + e.getMessage());
        }
    }

}
