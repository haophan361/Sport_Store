package com.sport_store.DTO.response.product_option_Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class product_option_admin_Response {
    private int option_id;
    private String color;
    private String size;
    private String cost;
    private int quantity;
    private int discount;
    private boolean discount_active;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDateTime time_start;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDateTime time_end;
    private String image;
    private boolean active;

}
