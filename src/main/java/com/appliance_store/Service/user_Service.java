package com.appliance_store.Service;

import com.appliance_store.DTO.request.register_account;
import com.appliance_store.Entity.User;
import com.appliance_store.Repository.user_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class user_Service {
    private final user_Repository user_repository;
    private final PasswordEncoder passwordEncoder;
    public void create_user(register_account request) {
        if(user_repository.findByEmail(request.getEmail())!=null) {
            throw new RuntimeException("Email đã tồn tại");
        }
        if(user_repository.findByPhone(request.getPhone())!=null) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }
        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .userName(request.getName())
                .userDateOfBirth(request.getDate_of_birth())
                .userGender(request.isGender())
                .userEmail(request.getEmail())
                .userPassword(passwordEncoder.encode(request.getPassword()))
                .userPhone(request.getPhone())
                .userRole(User.UserRole.CUSTOMER)
                .isActive(true).build();
        user_repository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getListUser() {
        return user_repository.findAll();
    }

    @PostAuthorize("returnObject.user_email==authentication.name")
    public User getUser(String userID) {
        return user_repository.findById(userID).orElse(null);
    }

    public User get_myInfo() {
        var context=SecurityContextHolder.getContext();
        String email=context.getAuthentication().getName();
        return user_repository.findByEmail(email);
    }

    public User getUserByEmail(String email) {
        return user_repository.findByEmail(email);
    }

    public User getUserByPhone(String phone) {
        return user_repository.findByPhone(phone);
    }
}
