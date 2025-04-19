package com.sport_store.DTO.response.product_option_Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class product_option_bill_supply_Response {
    private int product_option_id;
    private String product_name;
    private String color;
    private String size;
}
