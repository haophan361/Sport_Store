package com.appliance_store.Entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    private String categoryId;
    private String categoryImage;
    private String categoryName;
}