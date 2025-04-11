package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_img")
@Entity
@IdClass(Product_Img.class)
public class Product_Img {
    @Id
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products products;

    @Id
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "color_id")
    private Colors colors;

    @Id
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "image_id")
    private Images images;
}
