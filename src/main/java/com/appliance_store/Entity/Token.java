package com.appliance_store.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {
    @Id
    private String tokenId;
    private String userToken;
    private LocalDateTime tokenExpirationTime;
    private String userEmail;
}