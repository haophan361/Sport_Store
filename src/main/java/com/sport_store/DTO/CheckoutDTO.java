package com.sport_store.DTO;

import lombok.Data;

@Data
public class CheckoutDTO {
    private String customerId;
    private String receiverId;
    private String couponId;
} 