package com.appliance_store.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="category")
public class Category
{
    @Id
    private String category_id;
    private String category_name;
    private String category_image;
    @OneToMany(mappedBy = "category")
    List<Products> products;
}
