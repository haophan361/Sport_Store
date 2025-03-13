package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Products {
    @Id
    private String product_id;
    private String product_name;
    private String product_detail;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brands brands;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories categories;
    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discounts discounts;
    @OneToMany(mappedBy = "products")
    private List<Comments> comments;
    @OneToMany(mappedBy = "products")
    private List<Product_Options> product_options;
    @OneToMany(mappedBy = "products")
    private List<Images> images;
}
