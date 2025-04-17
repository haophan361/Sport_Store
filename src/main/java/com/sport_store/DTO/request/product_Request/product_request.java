package com.sport_store.DTO.request.product_Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class product_request {
    @NotNull(message = "Mã sản phẩm không được để trống")
    @NotBlank(message = "Mã sản phẩm không được để trống")
    private String product_id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String product_name;

    private String description;

    @NotBlank(message = "Loại sản phẩm không được để trống")
    private String category_id;

    @NotBlank(message = "Thương hiệu không được để trống")
    private String brand_id;

    private boolean active;
}
