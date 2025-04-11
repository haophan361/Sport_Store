package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.*;

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
    private boolean is_active;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brands brands;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories categories;
    @OneToMany(mappedBy = "products")
    private List<Comments> comments;
    @OneToMany(mappedBy = "products", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Product_Options> product_options;
    @OneToMany(mappedBy = "products", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Product_Img> product_img;
}
