package com.appliance_store.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {
    public enum UserRole
    {
        CUSTOMER,
        ADMIN,
        EMPLOYEE
    }
    @Id
    private String userId;
    private String userEmail;
    private String userPassword;
    private String userName;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private boolean userGender;
    private LocalDate userDateOfBirth;
    private String userPhone;
    private boolean isActive;
}