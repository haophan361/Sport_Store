package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "brands")
public class Brands {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int brand_id;
    public String brand_name;
    @ToString.Exclude
    @OneToMany(mappedBy = "brands")
    private List<Products> products;
}
