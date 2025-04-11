package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "images")
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int image_id;
    private String image_url;
    @OneToMany(mappedBy = "images", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Product_Img> product_img;
}
