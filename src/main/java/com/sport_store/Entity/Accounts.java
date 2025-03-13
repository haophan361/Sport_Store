package com.sport_store.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Accounts {
    public enum Role {
        CUSTOMER,
        ADMIN,
        EMPLOYEE
    }

    @Id
    private String email;
    private String password;
    private boolean is_active;
    private boolean is_online;
    @Enumerated(EnumType.STRING)
    private Accounts.Role role;
}
