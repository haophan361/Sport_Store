package com.sport_store.Service;

import com.sport_store.DTO.request.UserDTO.updateUser_request;
import com.sport_store.Entity.Users;
import com.sport_store.Repository.user_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class user_Service {
    private final user_Repository user_repository;
    private final PasswordEncoder passwordEncoder;

    public void create_user(Users user) {

        if (user_repository.findByEmail(user.getUser_email()) != null) {
            throw new RuntimeException("Email đã tồn tại");
        }
        if (user_repository.findByPhone(user.getUser_phone()) != null) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }
        user_repository.save(user);
    }

    public void changePassword(Users user, String new_password) {
        user.setUser_password(passwordEncoder.encode(new_password));
        user_repository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Users> getListUser() {
        return user_repository.findAll();
    }

    @PostAuthorize("returnObject.user_email==authentication.name")
    public Users getUser(String userID) {
        return user_repository.findById(userID).orElse(null);
    }

    public Users get_myInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        return user_repository.findByEmail(email);
    }

    public Users getUserByEmail(String email) {
        return user_repository.findByEmail(email);
    }

    public void updateUser(updateUser_request request) {
        Users user = get_myInfo();
        user.setUser_name(request.getUser_name());
        user.setUser_gender(request.isUser_gender());
        user.setUser_phone(request.getUser_phone());
        user.setUser_date_of_birth(request.getUser_date_of_birth());
        user_repository.save(user);
    }

    public boolean existByEmail(String email) {
        return user_repository.findByEmail(email) != null;
    }
}
