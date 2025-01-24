package com.sport_store.Repository;

import com.sport_store.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface product_Repository extends JpaRepository<Users, String> {

}
