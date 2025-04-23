package com.sport_store.DTO.response.bill_detail_Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class manage_bill_detail_Response {
    private String bill_id;
    private String bill_detail_id;
    private String product_name;
    private int option_id;
    private String color;
    private String size;
    private int stock;
    private BigDecimal price;
    private int quantity;
    private BigDecimal total_price;
    private BigDecimal bill_total_price;
}
