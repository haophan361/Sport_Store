package com.sport_store.DTO.response.info_receiver_Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class info_receiver_Response {
    private String infoReceiver_id;
    private String name;
    private String address;
    private String phone;
}
