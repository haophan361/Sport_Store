package com.sport_store.DTO.request.Info_receiver_Request;

import com.sport_store.Entity.Customers;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class infoReceiver_request {
    private String receiver_id;
    @NotBlank(message = "Họ và tên không được để trống")
    private String name;
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại phải đủ 10 số")
    private String phone;
    @NotBlank(message = "Tỉnh/Thành phố không được để trống")
    private String city;
    @NotBlank(message = "Quận/Huyện không được để trống")
    private String district;
    @NotBlank(message = "Phường/Xã không được để trống")
    private String ward;
    @NotBlank(message = "Tên đường không được để trống")
    @Size(max = 100, message = "Chỉ được nhập tối 100 kí tự")
    private String street;
    private Customers customer;
}
