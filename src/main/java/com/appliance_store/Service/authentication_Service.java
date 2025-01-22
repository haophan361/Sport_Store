package com.appliance_store.Service;

import com.appliance_store.DTO.request.authentication_request;
import com.appliance_store.DTO.response.authentication_response;
import com.appliance_store.DTO.response.user_response;
import com.appliance_store.Entity.Token;
import com.appliance_store.Entity.User;
import com.appliance_store.Repository.token_Repository;
import com.appliance_store.Repository.user_Repository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class authentication_Service {
    @Value("${jwt.signerKey}")
    private String signerKey;
    @Value("${jwt.valid-duration}")
    private long validDuration;
    @Value("${jwt.refreshable-duration}")
    private long refreshDuration;
    private final user_Repository user_repository;
    private final PasswordEncoder passwordEncoder;
    private final user_Service user_service;
    private final token_Repository token_repository;

    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier=new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT=SignedJWT.parse(token);
        Date expiration_time;
        if (isRefresh) {
             expiration_time=new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(refreshDuration,ChronoUnit.HOURS).toEpochMilli());
        } else {
            expiration_time=signedJWT.getJWTClaimsSet().getExpirationTime();
        }
        var verified =signedJWT.verify(verifier);
        if (!(verified && expiration_time.after(new Date()))) {
            throw new JOSEException("Token đã hết hạn");
        }
        return signedJWT;
    }

    public authentication_response authenticate(authentication_request request) throws Exception {
        User user = user_repository.findByEmail(request.getEmail());
        if (user == null) {
            throw new Exception("Tài khoản không tồn tại");
        } else {
            boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getUserPassword());
            if (!authenticated) {
                throw new Exception("Mật khẩu không hợp lệ");
            }
            String token=generateToken(user);
            user_response userResponse=  user_response.builder()
                    .ID(user.getUserId())
                    .name(user.getUserName())
                    .gender(user.isUserGender())
                    .date_of_birth(user.getUserDateOfBirth())
                    .phone(user.getUserPhone())
                    .email(user.getUserEmail())
                    .password(user.getUserPassword())
                    .role(user.getUserRole().toString())
                    .build();
            return authentication_response.builder()
                    .token(token)
                    .user_response(userResponse)
                    .build();
        }
    }

    private String generateToken(User user) {
        JWSHeader header=new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet=new JWTClaimsSet.Builder()
                .subject(user.getUserEmail())
                .issuer("sport_store.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(validDuration, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", user.getUserRole().toString())
                .build();
        Payload payload=new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject=new JWSObject(header,payload);
        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String refreshToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT=verifyToken(token,true);
        String email=signedJWT.getJWTClaimsSet().getSubject();
        User user = user_service.getUserByEmail(email);

        Token tokenEntity=token_repository.findTokenByID(signedJWT.getJWTClaimsSet().getJWTID());
        token_repository.delete(tokenEntity);
        String new_token= generateToken(user);
        SignedJWT newSignedJWT=SignedJWT.parse(new_token);
        Token newTokenEntity=Token.builder()
                .tokenId(newSignedJWT.getJWTClaimsSet().getJWTID())
                .tokenExpirationTime(LocalDateTime.now().plusHours(validDuration))
                .userToken(new_token)
                .userEmail(email)
                .build();
        token_repository.save(newTokenEntity);
        return new_token;
    }
}
