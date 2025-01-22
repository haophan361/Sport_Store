package com.appliance_store.DTO.response;

import com.appliance_store.Entity.ReceiverInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class user_response {
    private String ID;
    private String name;
    private String email;
    private String password;
    private String phone;
    private boolean gender;
    private LocalDate date_of_birth;
    private boolean active;
    private String role;
    @JsonIgnore
    private List<ReceiverInfo> infoReceivers;
}
