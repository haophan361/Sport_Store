package com.sport_store.DTO.response.cart_Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class cart_checkout_Response {
    private String cart_id;
    private String product_name;
    private int quantity;
    private String color;
    private String size;
    private BigDecimal price;
}
