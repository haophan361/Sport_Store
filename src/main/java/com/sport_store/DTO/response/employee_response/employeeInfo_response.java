package com.sport_store.DTO.response.employee_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class employeeInfo_response {
    private String name;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String address;
    private boolean gender;
    private boolean isOnline;
    private boolean isActive;
}
