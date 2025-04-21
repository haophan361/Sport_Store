package com.sport_store.Controller.controller;

import com.sport_store.Entity.Coupons;
import com.sport_store.Service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CouponAdminController {

    @Autowired
    private CouponService couponService;

    // Hiển thị trang quản lý mã giảm giá
    @GetMapping("/admin/manageCoupon")
    public String showCouponManagementPage(Model model) {
        List<Coupons> coupons = couponService.getAllCoupons();
        model.addAttribute("coupons", coupons);
        return "admin/coupon";
    }

    // API để lấy tất cả mã giảm giá
    @GetMapping("/admin/api/coupons")
    @ResponseBody
    public ResponseEntity<List<Coupons>> getAllCoupons() {
        List<Coupons> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    // API để thêm mã giảm giá mới
    @PostMapping("/admin/api/coupons/add")
    @ResponseBody
    public ResponseEntity<?> addCoupon(@RequestBody CouponDTO couponDTO) {
        try {
            // Kiểm tra xem couponId đã tồn tại chưa
            if (couponService.findCouponById(couponDTO.getCouponId()) != null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Mã giảm giá đã tồn tại");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Kiểm tra ngày bắt đầu phải trước ngày hết hạn
            if (couponDTO.getStartDate().isAfter(couponDTO.getExpirationDate())) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Ngày bắt đầu phải trước ngày hết hạn");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Tạo đối tượng Coupons mới
            Coupons coupon = new Coupons();
            coupon.setCoupon_id(couponDTO.getCouponId());
            coupon.setCoupon_percentage(couponDTO.getPercentage());
            coupon.setCoupon_start_date(couponDTO.getStartDate());
            coupon.setCoupon_expiration_date(couponDTO.getExpirationDate());
            coupon.setCoupon_attempts_left(couponDTO.getAttemptsLeft());

            // Lưu vào database
            couponService.saveCoupon(coupon);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lỗi khi thêm mã giảm giá: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // API để cập nhật mã giảm giá
    @PutMapping("/admin/api/coupons/update")
    @ResponseBody
    public ResponseEntity<?> updateCoupon(@RequestBody CouponDTO couponDTO) {
        try {
            // Kiểm tra xem couponId có tồn tại không
            Coupons existingCoupon = couponService.findCouponById(couponDTO.getCouponId());
            if (existingCoupon == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Không tìm thấy mã giảm giá");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Kiểm tra ngày bắt đầu phải trước ngày hết hạn
            if (couponDTO.getStartDate().isAfter(couponDTO.getExpirationDate())) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Ngày bắt đầu phải trước ngày hết hạn");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Cập nhật thông tin
            existingCoupon.setCoupon_percentage(couponDTO.getPercentage());
            existingCoupon.setCoupon_start_date(couponDTO.getStartDate());
            existingCoupon.setCoupon_expiration_date(couponDTO.getExpirationDate());
            existingCoupon.setCoupon_attempts_left(couponDTO.getAttemptsLeft());

            // Lưu vào database
            couponService.saveCoupon(existingCoupon);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lỗi khi cập nhật mã giảm giá: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // API để xóa mã giảm giá
    @DeleteMapping("/admin/api/coupons/delete/{couponId}")
    @ResponseBody
    public ResponseEntity<?> deleteCoupon(@PathVariable String couponId) {
        try {
            // Kiểm tra xem couponId có tồn tại không
            Coupons existingCoupon = couponService.findCouponById(couponId);
            if (existingCoupon == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Không tìm thấy mã giảm giá");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Xóa mã giảm giá
            couponService.deleteCoupon(couponId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lỗi khi xóa mã giảm giá: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DTO để nhận dữ liệu từ client
    public static class CouponDTO {
        private String couponId;
        private int percentage;
        private LocalDateTime startDate;
        private LocalDateTime expirationDate;
        private int attemptsLeft;

        public String getCouponId() {
            return couponId;
        }

        public void setCouponId(String couponId) {
            this.couponId = couponId;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        public LocalDateTime getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(LocalDateTime expirationDate) {
            this.expirationDate = expirationDate;
        }

        public int getAttemptsLeft() {
            return attemptsLeft;
        }

        public void setAttemptsLeft(int attemptsLeft) {
            this.attemptsLeft = attemptsLeft;
        }
    }
} 