package com.sport_store.Configuration;

import com.sport_store.DTO.request.UserDTO.register_account;
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
                register_account request = register_account.builder()
                        .ID(UUID.randomUUID().toString())
                        .email("admin@gmail.com")
                        .password("admin")
                        .role(Users.Role.ADMIN.toString())
                        .build();
                user_service.create_user(request);
                log.warn("Người dùng ADMIN đã được tạo tự động");
            }
        };
    }
}
