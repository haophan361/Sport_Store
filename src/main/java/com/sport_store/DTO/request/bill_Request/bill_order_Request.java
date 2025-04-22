package com.sport_store.DTO.request.bill_Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class bill_order_Request {
    private String receiver_id;
    private boolean payment_method;
}
