package com.sport_store.DTO.response.product_Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class product_admin_Response {
    private String product_id;
    private String product_name;
    private String category;
    private String brand;
    private boolean active;
}
