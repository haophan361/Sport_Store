package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
public class Carts {
    @Id
    private String cart_id;
    private int cart_quantity;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customers customers;
    @ManyToOne
    @JoinColumn(name = "option_id")
    private Product_Options product_options;
}
