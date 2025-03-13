package com.sport_store.DTO.request.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class updateCustomer_request {
    private String customer_name;
    private LocalDate customer_date_of_birth;
    private String customer_phone;
}
