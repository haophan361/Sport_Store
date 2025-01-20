package com.appliance_store.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "product_option")
@NoArgsConstructor
@AllArgsConstructor
public class Product_Option
{
    @Id
    private String option_id;
    private String option_color;
    private String option_size;
    private int option_quantity;
    private BigDecimal option_cost;
    private boolean is_active;
    private LocalDateTime create_date;
    @ManyToOne
    @JoinColumn(name="product_id")
    private Products products;
    @OneToMany(mappedBy = "product_option")
    private List<Carts> carts;
    @OneToMany(mappedBy = "product_option")
    private List<Image> images;
    @OneToMany(mappedBy = "product_option")
    private List<Bill_Details> bill_details;
}
