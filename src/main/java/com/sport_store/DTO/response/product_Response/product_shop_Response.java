package com.sport_store.DTO.response.product_Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class product_shop_Response {
    private String product_id;
    private String product_name;
    private String image_url;
    private String price;
    private String new_price;
    private int rating;
    private int num_rating;
    private String default_url_option;
}
