package com.sport_store.DTO.request.account_Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class changePassword_request {
    @NotBlank(message = "Mật khẩu cũ không được để trống")
    private String old_password;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 5, message = "Mật khẩu cần ít nhất 5 kí tự")
    @Size(max = 100, message = "Chỉ nhập tối đa 100 kí tự cho mật khẩu")
    private String new_password;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    @Size(min = 5, message = "Mật khẩu cần ít nhất 5 kí tự")
    @Size(max = 100, message = "Chỉ nhập tối đa 100 kí tự cho mật khẩu")
    private String confirm_password;

}
