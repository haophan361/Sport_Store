package com.appliance_store.Service;

import com.appliance_store.DTO.request.register_account;
import com.appliance_store.Entity.Users;
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
public class user_Service
{
    private final user_Repository user_repository;
    private final PasswordEncoder passwordEncoder;
    public void create_user(register_account request)
    {
        if(user_repository.findByEmail(request.getEmail())!=null)
        {
            throw new RuntimeException("Email đã tồn tại");
        }
        if(user_repository.findByPhone(request.getPhone())!=null)
        {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }
        Users user = Users.builder()
                .user_id(UUID.randomUUID().toString())
                .user_name(request.getName())
                .user_date_of_birth(request.getDate_of_birth())
                .user_gender(request.isGender())
                .user_email(request.getEmail())
                .user_password(passwordEncoder.encode(request.getPassword()))
                .user_phone(request.getPhone())
                .user_role(Users.Role.CUSTOMER)
                .is_active(true).build();
        user_repository.save(user);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<Users> getListUser()
    {
        return user_repository.findAll();
    }
    @PostAuthorize("returnObject.user_email==authentication.name")
    public Users getUser(String userID)
    {
        return user_repository.findById(userID).orElse(null);
    }
    public Users get_myInfo()
    {
        var context=SecurityContextHolder.getContext();
        String email=context.getAuthentication().getName();
        return user_repository.findByEmail(email);
    }
    public Users getUserByEmail(String email)
    {
        return user_repository.findByEmail(email);
    }
    public Users getUserByPhone(String phone)
    {
        return user_repository.findByPhone(phone);
    }
}
