package com.sport_store.Controller.api;

import com.sport_store.DTO.request.discount_Request.discount_request;
import com.sport_store.Entity.Discounts;
import com.sport_store.Service.discount_Service;
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
public class discount_API {
    private final discount_Service discount_service;

    @PostMapping("/admin/insert_discount")
    public ResponseEntity<?> insert_discount(@RequestBody discount_request request) {
        discount_service.saveDiscount(request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm giảm giá thành công"));
    }

    @PostMapping("/admin/delete_discount")
    public ResponseEntity<?> delete_discount(@RequestBody int discount_id) {
        discount_service.deleteDiscount(discount_id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Xóa giảm giá thành công"));
    }

    @GetMapping("/getAllDiscount")
    public List<Discounts> getAllDiscount() {
        return discount_service.getAllDiscounts();
    }
}
