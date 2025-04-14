package com.sport_store.DTO.request.employee_request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class create_employee {
    @NotBlank(message = "Tên nhân viên không được để trống")
    private String employee_name;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String employee_address;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String employee_phone;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String employee_email;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate employee_date_of_birth;

    @NotNull(message = "Giới tính không được để trống")
    private Boolean employee_gender;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
