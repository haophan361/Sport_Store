package com.sport_store.Repository;

import com.sport_store.Entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface customer_Repository extends JpaRepository<Customers, String> {
    @Query("SELECT c FROM Customers c WHERE c.customer_phone= :phone")
    public Customers findByPhone(String phone);

    @Query("SELECT c FROM Customers c WHERE c.customer_email =:email")
    public Customers findByEmail(String email);

    @Query("SELECT c FROM Customers c WHERE c.customer_email =:email")
    Optional<Customers> findByUsername(String email);
}
