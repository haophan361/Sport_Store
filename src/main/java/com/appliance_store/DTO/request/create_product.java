package com.appliance_store.DTO.request;

import com.appliance_store.Entity.Brand;
import com.appliance_store.Entity.Category;
import com.appliance_store.Entity.Discount;
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
    private Category category;
    private Brand brand;
    private Discount discount;
}
