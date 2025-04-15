package com.sport_store.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discounts")
public class Discounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int discount_id;
    private int discount_percentage;
    private LocalDateTime discount_start_date;
    private LocalDateTime discount_end_date;
    private boolean is_active;
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "discounts")
    List<Product_Options> product_options;
}
