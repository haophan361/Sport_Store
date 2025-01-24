package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")

public class Users {
    public enum Role {
        CUSTOMER,
        ADMIN,
        EMPLOYEE
    }

    @Id
    private String user_id;
    private String user_email;
    private String user_password;
    private String user_name;
    @Enumerated(EnumType.STRING)
    private Role user_role;
    private boolean user_gender;
    private LocalDate user_date_of_birth;
    private String user_phone;
    private boolean is_active;
    @OneToMany(mappedBy = "users")
    private List<Receiver_Info> Receiver_Info;
    @OneToMany(mappedBy = "users")
    private List<Carts> carts;
    @OneToMany(mappedBy = "users")
    private List<Comments> comments;
}
