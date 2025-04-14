package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "product_options")
@NoArgsConstructor
@AllArgsConstructor
public class Product_Options {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int option_id;
    private String option_size;
    private int option_quantity;
    private BigDecimal option_cost;
    private boolean is_active;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "product_id")
    private Products products;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "color_id")
    private Colors colors;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discounts discounts;
    @OneToMany(mappedBy = "product_options")
    private List<Carts> carts;
    @OneToMany(mappedBy = "product_options")
    private List<Bill_Details> bill_details;
}
