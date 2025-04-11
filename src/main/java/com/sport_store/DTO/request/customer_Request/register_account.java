package com.sport_store.DTO.request.customer_Request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class register_account {
    private String ID;
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Phải nhập email theo đúng định dạng")
    @Size(max = 100, message = "Chỉ nhập tối đa 100 kí tự cho Email")
    private String email;
    @Size(min = 5, message = "Mật khẩu cần ít nhất 5 kí tự")
    @Size(max = 100, message = "Chỉ nhập tối đa 100 kí tự cho mật khẩu")
    private String password;
    private String name;
    private String role;
    @NotNull(message = "Giới tính phải được chọn")
    private boolean gender;
    private LocalDate date_of_birth;
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại phải đủ 10 số")
    private String phone;
    private boolean active;
}
