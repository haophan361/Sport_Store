package com.sport_store.Controller.controller;

import com.sport_store.Service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CouponController {
    
    private final CouponService couponService;
    
    @PostMapping("/api/apply-coupon")
    public ResponseEntity<?> applyCoupon(@RequestParam("couponId") String couponId) {
        Map<String, Object> response = new HashMap<>();
        
        if (couponService.validateCoupon(couponId)) {
            int percentage = couponService.getCouponPercentage(couponId);
            response.put("valid", true);
            response.put("percentage", percentage);
            return ResponseEntity.ok(response);
        } else {
            response.put("valid", false);
            response.put("message", "Coupon không hợp lệ hoặc đã hết hạn");
            return ResponseEntity.badRequest().body(response);
        }
    }
} 