package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bills")
public class Bills {
    @Id
    private String bill_id;
    private BigDecimal bill_total_cost;
    private LocalDateTime bill_purchase_date;
    private LocalDateTime bill_confirm_date;
    private boolean bill_status;
    private LocalDateTime bill_receive_date;
    private boolean is_active;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Users employee;
    @OneToMany(mappedBy = "bills")
    private List<Bill_Details> bill_details;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Receiver_Info receiver;
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupons coupon;
}
