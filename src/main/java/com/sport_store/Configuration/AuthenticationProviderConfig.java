package com.sport_store.Configuration;

import com.sport_store.Entity.Accounts;
import com.sport_store.Repository.account_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthenticationProviderConfig {
    private final account_Repository account_repository;

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public UserDetailsService getUserDetailsService() {
        return email -> {
            Accounts account = account_repository.findAccountsByEmail_Optional(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng có Email: " + email));
            return User.builder()
                    .username(account.getEmail())
                    .password(account.getPassword())
                    .roles(account.getRole().toString())
                    .build();
        };
    }
}
