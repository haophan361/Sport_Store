package com.sport_store.DTO.response.product_Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class product_update_Response {
    private String product_id;
    private String product_name;
    private String product_detail;
    private boolean active;
    private int category_id;
    private String category_name;
    private int brand_id;
    private String brand_name;
}
