package com.appliance_store.Repository;

import com.appliance_store.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface product_Repository extends JpaRepository<User,String> {

}
