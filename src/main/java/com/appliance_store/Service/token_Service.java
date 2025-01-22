package com.appliance_store.Service;

import com.appliance_store.Entity.Token;
import com.appliance_store.Repository.token_Repository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class token_Service {
    private final token_Repository token_repository;
    public void createToken(Token token) {
        token_repository.save(token);
    }

    public Token findTokenByID(String tokenID) {
        return token_repository.findTokenByID(tokenID);
    }

    public boolean deleteToken(Token token) {
        token_repository.delete(token);
        return true;
    }

    @Scheduled(fixedRate = 300000)
    public void cleanUpExpiredToken() {
        List<Token> token_expired=token_repository.findTokenByExpirationTime(LocalDateTime.now());
        for(Token token:token_expired) {
            deleteToken(token);
        }
    }
}
