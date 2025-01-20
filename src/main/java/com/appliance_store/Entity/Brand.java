package com.appliance_store.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="brand")
public class Brand
{
    @Id
    public String brand_id;
    public String brand_name;
    @OneToMany(mappedBy = "brand")
    private List<Products> products;
}
