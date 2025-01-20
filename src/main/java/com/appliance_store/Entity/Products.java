package com.appliance_store.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="products")
public class Products
{
    @Id
    private String product_id;
    private String product_name;
    private String product_detail;
    @ManyToOne
    @JoinColumn(name="brand_id")
    private Brand brand;
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name="discount_id")
    private Discount discount;
    @OneToMany(mappedBy = "products")
    private List<Comments> comments;
    @OneToMany(mappedBy = "products")
    private List<Product_Option> product_options;
}
