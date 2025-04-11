package com.sport_store.DTO.response.product_Response;

import com.sport_store.DTO.response.product_option_Response.product_option_detail_Response;
import com.sport_store.Entity.Brands;
import com.sport_store.Entity.Categories;
import com.sport_store.Entity.Discounts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class product_detail_Response {
    private String product_id;
    private String product_name;
    private String product_price;
    private String description;
    private Brands brand;
    private Categories category;
    private product_option_detail_Response current_option;
    private Discounts discount;
}
