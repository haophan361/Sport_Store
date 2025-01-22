package com.appliance_store.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    private String couponId;
    private int couponPercentage;
    private LocalDateTime couponStartDate;
    private LocalDateTime couponExpirationDate;
    private int couponAttemptsLeft;
}