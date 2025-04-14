package com.sport_store.DTO.response.product_option_Response;

import com.sport_store.Entity.Colors;
import com.sport_store.Entity.Product_Img;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class product_option_detail_Response {
    private int option_id;
    private String price;
    private String new_price;
    private List<Product_Img> product_img;
    private Colors color;
    private String size;
}
