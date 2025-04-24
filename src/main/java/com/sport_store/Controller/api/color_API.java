package com.sport_store.Controller.api;

import com.sport_store.DTO.request.color_Request.color_Request;
import com.sport_store.DTO.response.color_Response.color_Response;
import com.sport_store.Entity.Colors;
import com.sport_store.Entity.Product_Img;
import com.sport_store.Service.color_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class color_API {
    private final color_Service color_service;

    @PostMapping("/admin/insert_color")
    public ResponseEntity<?> insert_Color(@RequestPart("color_request") color_Request request, @RequestPart(value = "image_url", required = false) MultipartFile[] files) {
        color_service.saveColor(request, files);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm màu thành công"));
    }

    @GetMapping("/getAllColor")
    public List<color_Response> getAllColor() {
        List<Colors> colors = color_service.getAllColors();
        List<color_Response> responses = new ArrayList<>();
        for (Colors c : colors) {
            List<String> image_url = new ArrayList<>();
            for (Product_Img product_img : c.getProduct_img()) {
                image_url.add(product_img.getImages().getImage_url());
            }
            color_Response response = color_Response
                    .builder()
                    .color_id(c.getColor_id())
                    .color(c.getColor())
                    .image_url(image_url)
                    .build();
            responses.add(response);
        }
        return responses;
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

    @PutMapping("/admin/update_color")
    public ResponseEntity<?> updateColor(@RequestPart("color_request") color_Request request,
                                         @RequestPart(value = "image_url", required = false) MultipartFile[] files) {
        color_service.updateColor(request, files);
        return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật màu sắc thành công"));
    }

}
