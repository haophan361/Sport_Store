package com.appliance_store.Configuration;

import com.appliance_store.Entity.User;
import com.appliance_store.Repository.user_Repository;
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
        return args -> {
            if (user_repository.findByEmail("admin@gmail.com") == null) {
                User user = User.builder()
                        .userId(UUID.randomUUID().toString())
                        .userEmail("admin@gmail.com")
                        .userPassword(passwordEncoder.encode("admin"))
                        .userRole(User.UserRole.ADMIN)
                        .build();
                user_repository.save(user);
                log.warn("Người dùng ADMIN đã được tạo tự động");
            }
        };
    }
}