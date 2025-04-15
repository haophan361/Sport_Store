package com.sport_store.DTO.request.product_option_Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class productOption_request {
    private int color_id;
    private String size;
    @NotNull(message = "Giá bán sản phẩm không được để trống")
    @Min(value = 0, message = "Giá bán sản phẩm không được nhỏ hơn 0")
    private BigDecimal option_price;
    @NotNull(message = "Số lượng sản phẩm không được để trống")
    @Min(value = 0, message = "Số lượng sản phẩm không được nhỏ hơn 0")
    private int option_quantity;
    private String product_id;
    private int discount_id;
    private boolean active;
}
