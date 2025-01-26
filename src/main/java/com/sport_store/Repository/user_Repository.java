package com.sport_store.Repository;

import com.sport_store.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface user_Repository extends JpaRepository<Users, String> {
    @Query("SELECT u FROM Users u WHERE u.user_phone= :phone")
    public Users findByPhone(String phone);

    @Query("SELECT u FROM Users u WHERE u.user_email =:email")
    public Users findByEmail(String email);

    @Query("SELECT u FROM Users u WHERE u.user_email =:email")
    Optional<Users> findByUsername(String email);
}
