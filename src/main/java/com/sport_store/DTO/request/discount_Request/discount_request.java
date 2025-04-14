package com.sport_store.DTO.request.discount_Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class discount_request {
    private int discount_percentage;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private boolean active;
}
