package com.sport_store.DTO.request.employee_request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class update_employee {
    @NotBlank(message = "Địa chỉ không được để trống")
    private String employee_address;
    @NotBlank(message = "Số điện thoại không được để trống")
    private String employee_phone;
}