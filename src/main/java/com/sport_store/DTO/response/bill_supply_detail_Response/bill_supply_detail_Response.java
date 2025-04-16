package com.sport_store.DTO.response.bill_supply_detail_Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class bill_supply_detail_Response {
    private String detail_id;
    private String product_name;
    private int option_id;
    private String size;
    private String color;
    private int quantity;
    private String cost;
    private String total_price;
}
