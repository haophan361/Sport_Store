package com.sport_store.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @OneToMany(mappedBy = "colors")
    private List<Product_Options> product_options;
    @OneToMany(mappedBy = "colors", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<Product_Img> product_img;
}
