package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "colors")
public class Colors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int color_id;
    private String color;
    @ToString.Exclude
    @OneToMany(mappedBy = "colors")
    private List<Product_Options> product_options;
    @OneToMany(mappedBy = "colors", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Product_Img> product_img;
}
