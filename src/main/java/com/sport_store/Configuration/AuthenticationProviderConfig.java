package com.sport_store.Configuration;

import com.sport_store.Entity.Users;
import com.sport_store.Repository.user_Repository;
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
    private final user_Repository user_repository;

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
            Users user = user_repository.findByUsername(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng có Email: " + email));
            return User.builder()
                    .username(user.getUser_email())
                    .password(user.getUser_password())
                    .roles(user.getUser_role().toString())
                    .build();
        };
    }
}
