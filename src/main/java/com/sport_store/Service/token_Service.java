package com.sport_store.Service;

import com.sport_store.Entity.Tokens;
import com.sport_store.Repository.token_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class token_Service {
    private final token_Repository token_repository;

    public void createToken(Tokens tokens) {
        token_repository.save(tokens);
    }

    public Tokens findTokenByID(String tokenID) {
        return token_repository.findTokenByID(tokenID);
    }

    public boolean deleteToken(Tokens tokens) {
        token_repository.delete(tokens);
        return true;
    }

    @Scheduled(fixedRate = 300000)
    public void cleanUpExpiredToken() {
        List<Tokens> tokens_expired = token_repository.findTokenByExpirationTime(LocalDateTime.now());
        for (Tokens tokens : tokens_expired) {
            deleteToken(tokens);
        }
    }
}
