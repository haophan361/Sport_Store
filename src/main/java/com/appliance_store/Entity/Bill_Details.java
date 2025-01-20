package com.appliance_store.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="bill_details")
public class Bill_Details
{
    private String product_name;
    private BigDecimal product_cost;
    private int product_quantity;
    @Id
    @ManyToOne
    @JoinColumn(name="bill_id")
    private Bills bills;
    @Id
    @ManyToOne
    @JoinColumn(name="product_option_id")
    private Product_Option product_option;
}
