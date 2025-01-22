package com.appliance_store.Repository;

import com.appliance_store.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface user_Repository extends JpaRepository<User,String> {
    @Query("SELECT u FROM User u WHERE u.userPhone= :phone")
    public User findByPhone(String phone);
    @Query("SELECT u FROM User u WHERE u.userEmail =:email")
    public User findByEmail(String email);
}
