package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bill_details")
public class Bill_Details {
    private String product_name;
    private BigDecimal product_cost;
    private int product_quantity;
    @Id
    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bills bills;
    @Id
    @ManyToOne
    @JoinColumn(name = "option_id")
    private Product_Options product_options;
}
