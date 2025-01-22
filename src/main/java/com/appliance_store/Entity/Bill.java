package com.appliance_store.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bills")
public class Bill {
    @Id
    private String billId;
    private BigDecimal billTotalCost;
    private LocalDateTime billPurchaseDate;
    private LocalDateTime billConfirmDate;
    private boolean billStatus;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private ReceiverInfo receiverInfo;

    private LocalDateTime billReceiveDate;
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}