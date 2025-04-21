package com.sport_store.Repository;

import com.sport_store.Entity.Coupons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupons, String> {
    @Query("SELECT c FROM Coupons c WHERE c.coupon_id = :couponId")
    Coupons findByCouponId(@Param("couponId") String couponId);
} 