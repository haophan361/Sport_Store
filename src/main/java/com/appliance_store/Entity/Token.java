package com.appliance_store.Entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tokens")
public class Token
{
    @Id
    private String token_id;
    private String user_token;
    private String user_email;
    private LocalDateTime token_expiration_time;
}
