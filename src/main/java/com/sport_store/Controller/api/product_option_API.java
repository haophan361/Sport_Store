package com.sport_store.Controller.api;

import com.sport_store.DTO.request.product_option_Request.productOption_request;
import com.sport_store.Service.product_option_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class product_option_API {
    private final product_option_Service product_option_service;

    @PostMapping("/admin/insert_product_option")
    public ResponseEntity<?> insert_product_options(@RequestPart("product_option_request") productOption_request request, @RequestPart("image_url") MultipartFile[] files) {
        product_option_service.Save_productOption(request, files);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm mẫu sản phẩm thành công"));
    }

    @PostMapping("/admin/delete_product_option")
    public ResponseEntity<?> delete_product_options(@RequestBody String option_id) {
        product_option_service.delete_product_option(option_id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Xóa mẫu sản phẩm thành công"));
    }
}
