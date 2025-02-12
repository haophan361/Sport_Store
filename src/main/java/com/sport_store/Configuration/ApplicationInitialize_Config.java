package com.sport_store.Configuration;

import com.sport_store.Entity.Users;
import com.sport_store.Service.user_Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitialize_Config {

    @Bean
    ApplicationRunner applicationRunner(user_Service user_service) {
        return args ->
        {
            if (!user_service.existByEmail("admin@gmail.com")) {
                Users user = Users.builder()
                        .user_id(UUID.randomUUID().toString())
                        .user_email("admin@gmail.com")
                        .user_password("admin")
                        .user_role(Users.Role.ADMIN)
                        .is_active(true)
                        .build();
                user_service.create_user(user);
                log.warn("Người dùng ADMIN đã được tạo tự động");
            }
        };
    }
}
