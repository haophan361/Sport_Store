package com.sport_store.DTO.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sport_store.Entity.Receiver_Info;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<Receiver_Info> infoReceivers;
}
