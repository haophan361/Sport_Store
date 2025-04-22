package com.sport_store.DTO.response.customer_response;

import com.sport_store.DTO.response.info_receiver_Response.info_receiver_changeInfo_Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class customer_changeInfo_Response {
    private String customer_id;
    private String customer_email;
    private String customer_name;
    private LocalDate customer_date_of_birth;
    private String customer_phone;
    List<info_receiver_changeInfo_Response> receiver_Info;
}
