package com.sport_store.DTO.response.account_Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class authentication_response {
    private String token;
    private com.sport_store.DTO.response.account_Response.account_response account_response;
}
