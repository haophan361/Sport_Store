package com.appliance_store.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="coupons")
public class Coupons
{
    @Id
    private String coupon_id;
    private int coupon_percentage;
    private LocalDateTime coupon_start_date;
    private LocalDateTime coupon_expiration_date;
    private int coupon_attempts_left;
    @OneToMany(mappedBy = "coupon")
    private List<Bills> bill;
}
