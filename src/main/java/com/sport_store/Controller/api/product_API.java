package com.sport_store.Controller.api;

import com.sport_store.DTO.request.product_Request.product_request;
import com.sport_store.Service.product_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class product_API {
    private final product_Service product_service;

    @PostMapping("/admin/insert_product")
    public ResponseEntity<?> insertProduct(@RequestBody product_request request) {
        product_service.Save_Product(request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm sản phẩm thành công"));
    }

    @PostMapping("/admin/delete_product")
    public ResponseEntity<?> deleteProduct(@RequestBody String product_id) {
        product_service.delete_Product(product_id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Xóa sản phẩm thành công"));
    }
}
