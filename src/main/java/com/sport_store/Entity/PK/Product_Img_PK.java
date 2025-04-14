package com.sport_store.Entity.PK;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product_Img_PK implements Serializable {
    private String product_id;
    private int color_id;
    private int image_id;
}
