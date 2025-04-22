package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.*;

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
    private boolean bill_status_payment;
    private LocalDateTime bill_receive_date;
    private boolean is_active;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "employee_id")
    private Employees employees;
    @OneToMany(mappedBy = "bills")
    @ToString.Exclude
    private List<Bill_Details> bill_details;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "receiver_id")
    private Receiver_Info receivers;
}
