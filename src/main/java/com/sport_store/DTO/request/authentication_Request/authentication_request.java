package com.sport_store.DTO.request.authentication_Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class authentication_request {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email phải hợp lệ")
    @Size(max = 100, message = "Chỉ nhập tối đa 100 kí tự cho Email")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 5, max = 100, message = "Mật khẩu phải ít nhất 5 ký tự và tối đa 100 ký tự")
    private String password;
}