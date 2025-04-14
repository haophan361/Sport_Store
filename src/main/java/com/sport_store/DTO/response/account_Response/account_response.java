package com.sport_store.DTO.response.account_Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class account_response {
    private String email;
    private String password;
    private boolean active;
    private String role;
}
