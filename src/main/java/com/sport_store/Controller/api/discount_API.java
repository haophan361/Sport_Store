package com.sport_store.Controller.api;

import com.sport_store.DTO.request.discount_Request.discount_request;
import com.sport_store.DTO.response.discount_Response.discount_Response;
import com.sport_store.Entity.Discounts;
import com.sport_store.Service.discount_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @PutMapping("/admin/delete_discount")
    public ResponseEntity<?> delete_discount(@RequestParam int discount_id) throws Exception {
        try {
            discount_service.deleteDiscount(discount_id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Xóa giảm giá thành công"));
        } catch (Exception e) {
            throw new Exception("Không thể xóa giảm giá do " + e.getMessage());
        }
    }

    @GetMapping("/getAllDiscount")
    public List<discount_Response> getAllDiscount() {
        List<Discounts> discounts = discount_service.getAllDiscounts();
        List<discount_Response> responses = new ArrayList<>();
        for (Discounts discount : discounts) {
            discount_Response response = discount_Response
                    .builder()
                    .discount_id(discount.getDiscount_id())
                    .discount_percentage(discount.getDiscount_percentage())
                    .discount_start_date(discount.getDiscount_start_date())
                    .discount_end_date(discount.getDiscount_end_date())
                    .build();
            responses.add(response);
        }
        return responses;
    }
}
