package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String option_id;
    private String option_color;
    private String option_size;
    private int option_quantity;
    private BigDecimal option_cost;
    private boolean is_active;
    private String option_image_url;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products products;
    @OneToMany(mappedBy = "product_options")
    private List<Carts> carts;
    @OneToMany(mappedBy = "product_options")
    private List<Bill_Details> bill_details;
}
