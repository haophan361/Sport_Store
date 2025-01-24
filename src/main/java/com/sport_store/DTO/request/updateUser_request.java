package com.sport_store.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class updateUser_request {
    private String user_name;
    private LocalDate user_date_of_birth;
    private boolean user_gender;
    private String user_phone;
}
