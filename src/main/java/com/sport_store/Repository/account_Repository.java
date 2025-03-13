package com.sport_store.Repository;

import com.sport_store.Entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface account_Repository extends JpaRepository<Accounts, String> {
    @Query("SELECT a FROM Accounts a WHERE a.email= :email")
    public Accounts findAccountsByEmail(String email);

    @Query("SELECT a FROM Accounts a WHERE a.email= :email")
    public Optional<Accounts> findAccountsByEmail_Optional(String email);

    @Query(" SELECT COUNT(a) > 0 FROM Accounts  a WHERE a.email= :email")
    public boolean existsByEmail(String email);
}
