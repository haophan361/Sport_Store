package com.sport_store.DTO.request.bill_supply_detail_Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class bill_supply_detail_Request {
    private String product_name;
    private int option_id;
    private BigDecimal cost;
    private int quantity;
}
