package com.sport_store.Service;

import com.sport_store.Entity.Coupons;
import com.sport_store.Repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public Coupons findCouponById(String couponId) {
        return couponRepository.findByCouponId(couponId);
    }

    public boolean validateCoupon(String couponId) {
        Coupons coupon = findCouponById(couponId);
        if (coupon == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        return coupon.getCoupon_start_date().isBefore(now) &&
               coupon.getCoupon_expiration_date().isAfter(now) &&
               coupon.getCoupon_attempts_left() > 0;
    }

    public int getCouponPercentage(String couponId) {
        Coupons coupon = findCouponById(couponId);
        return coupon != null ? coupon.getCoupon_percentage() : 0;
    }

    public void decrementCouponAttempts(String couponId) {
        Coupons coupon = findCouponById(couponId);
        if (coupon != null && coupon.getCoupon_attempts_left() > 0) {
            coupon.setCoupon_attempts_left(coupon.getCoupon_attempts_left() - 1);
            couponRepository.save(coupon);
        }
    }
} 