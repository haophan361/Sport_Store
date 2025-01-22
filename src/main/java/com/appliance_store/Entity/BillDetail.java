package com.appliance_store.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bill_details")
public class BillDetail {
    @Id
    private String billDetailId;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    private String productName;
    private BigDecimal productCost;
    private int productQuantity;
}