package com.sport_store.Configuration;

import com.sport_store.Entity.Users;
import com.sport_store.Repository.user_Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitialize_Config {
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(user_Repository user_repository) {
        return args ->
        {
            if (user_repository.findByEmail("admin@gmail.com") == null) {
                Users user = Users.builder()
                        .user_id(UUID.randomUUID().toString())
                        .user_email("admin@gmail.com")
                        .user_password(passwordEncoder.encode("admin"))
                        .user_role(Users.Role.ADMIN)
                        .build();
                user_repository.save(user);
                log.warn("Người dùng ADMIN đã được tạo tự động");
            }
        };
    }
}
