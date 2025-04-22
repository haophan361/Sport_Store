package com.sport_store.DTO.response.employee_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class employee_changeInfo_Response {
    private String employee_id;
    private String employee_name;
    private String employee_email;
    private String employee_phone;
    private String employee_address;
    private LocalDate employee_date_of_birth;
    private boolean employee_gender;
}
