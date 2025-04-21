package com.sport_store.DTO.request.discount_Request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Phần trăm khuyến mãi không được để trống")
    @Min(value = 0, message = "Phần trăm khuyến mãi ít nhất 0")
    @Max(value = 100, message = "Phần trăm khuyến mãi không vượt quá 100")
    private int discount_percentage;

    @NotNull(message = "Ngày bắt đầu không được null")
    @FutureOrPresent(message = "Ngày bắt đầu phải là hôm nay hoặc tương lai")
    private LocalDateTime start_date;

    @NotNull(message = "Ngày kết thúc không được null")
    @FutureOrPresent(message = "Ngày kết thúc phải là hôm nay hoặc tương lai")
    private LocalDateTime end_date;

    private boolean active;
}
