package com.sport_store.DTO.request.color_Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class color_Request {
    private int color_id;
    private String color;
    private String product_id;
}
