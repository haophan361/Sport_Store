package com.sport_store.DTO.response.product_option_Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class product_option_update_Response {
    private int product_option_id;
    private int color_id;
    private String color;
    private Integer discount_id;
    private int discount_percentage;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime start;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime end;
    private String size;
    private BigDecimal price;
    private List<String> img_url;
}
