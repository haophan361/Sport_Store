package com.sport_store.Configuration;

import com.sport_store.Entity.Accounts;
import com.sport_store.Service.account_Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitialize_Config {

    @Bean
    ApplicationRunner applicationRunner(account_Service account_service) {
        return args ->
        {
            account_service.updateOnline();
            if (!account_service.existByEmail("admin@gmail.com")) {
                Accounts account = Accounts.builder()
                        .email("admin@gmail.com")
                        .password("admin")
                        .role(Accounts.Role.ADMIN)
                        .is_active(true)
                        .build();
                account_service.create_account(account);
                log.warn("Người dùng ADMIN đã được tạo tự động");
            }
        };
    }
}
