package com.sport_store.Repository;

import com.sport_store.Entity.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface token_Repository extends JpaRepository<Tokens, String> {

    @Query("SELECT t FROM Tokens  t WHERE t.token_expiration_time < :expirationTime")
    public List<Tokens> findTokenByExpirationTime(@Param("expirationTime") LocalDateTime expirationTime);

    @Query("SELECT t FROM Tokens t WHERE t.token_id =:tokenID")
    public Tokens findTokenByID(@Param("tokenID") String tokenID);
}
