package com.sport_store.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String discount_id;
    private int discount_percentage;
    private LocalDateTime discount_start_date;
    private LocalDateTime discount_end_date;
    private boolean is_active;
    @OneToMany(mappedBy = "discounts")
    List<Products> products;
}
