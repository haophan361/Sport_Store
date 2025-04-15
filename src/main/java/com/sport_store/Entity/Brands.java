package com.sport_store.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @OneToMany(mappedBy = "brands")
    private List<Products> products;
}
