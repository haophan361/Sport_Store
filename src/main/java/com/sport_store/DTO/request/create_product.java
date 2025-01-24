package com.sport_store.DTO.request;

import com.sport_store.Entity.Brands;
import com.sport_store.Entity.Categories;
import com.sport_store.Entity.Discounts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class create_product {
    private String ID;
    private String product_name;
    private String detail;
    private BigDecimal cost;
    private int quantity;
    private boolean active;
    private LocalDateTime create_date;
    private Categories categories;
    private Brands brand;
    private Discounts discounts;
}
