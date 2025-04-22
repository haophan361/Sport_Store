package com.sport_store.DTO.response.cart_Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class cart_Response {
    private String cart_id;
    private String image_url;
    private String product_name;
    private String color;
    private String size;
    private BigDecimal cost;
    private int quantity;
    private BigDecimal total_price;
}
