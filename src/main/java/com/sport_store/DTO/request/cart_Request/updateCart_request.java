package com.sport_store.DTO.request.cart_Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class updateCart_request {
    private String cart_id;
    private int quantity;
}
