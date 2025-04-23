package com.sport_store.DTO.request.cart_Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class addCart_request {
    private int option_id;
    private int quantity;
}
