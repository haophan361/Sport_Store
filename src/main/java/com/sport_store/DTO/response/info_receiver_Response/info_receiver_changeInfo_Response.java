package com.sport_store.DTO.response.info_receiver_Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class info_receiver_changeInfo_Response {
    private String receiver_id;
    private String receiver_name;
    private String receiver_phone;
    private String receiver_city;
    private String receiver_district;
    private String receiver_ward;
    private String receiver_street;
}
