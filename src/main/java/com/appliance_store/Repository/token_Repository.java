package com.appliance_store.Repository;

import com.appliance_store.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface token_Repository extends JpaRepository<Token,Integer> {
    @Query("SELECT t FROM Token t WHERE t.userEmail =:email")
    public Token findTokenByEmail(@Param("email") String email);
    @Query("SELECT t FROM Token  t WHERE t.tokenExpirationTime < :expirationTime")
    public List<Token> findTokenByExpirationTime(@Param("expirationTime") LocalDateTime expirationTime);
    @Query("SELECT t FROM Token t WHERE t.tokenId =:tokenID")
    public Token findTokenByID(@Param("tokenID") String tokenID);
}
