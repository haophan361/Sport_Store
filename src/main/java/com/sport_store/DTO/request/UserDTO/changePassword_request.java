package com.sport_store.DTO.request.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class changePassword_request {
    private String old_password;
    private String new_password;
    private String confirm_password;
}
