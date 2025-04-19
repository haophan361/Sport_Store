package com.sport_store.Controller.api;

import com.sport_store.Entity.Colors;
import com.sport_store.Service.color_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


}
