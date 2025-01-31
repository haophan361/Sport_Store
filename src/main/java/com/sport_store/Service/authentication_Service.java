package com.sport_store.Service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sport_store.DTO.request.AuthenticationDTO.authentication_request;
import com.sport_store.DTO.request.UserDTO.register_account;
import com.sport_store.DTO.response.authentication_response;
import com.sport_store.DTO.response.user_response;
import com.sport_store.Entity.Tokens;
import com.sport_store.Entity.Users;
import com.sport_store.Repository.token_Repository;
import com.sport_store.Repository.user_Repository;
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
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiration_time;
        if (isRefresh) {
            expiration_time = new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(refreshDuration, ChronoUnit.HOURS).toEpochMilli());
        } else {
            expiration_time = signedJWT.getJWTClaimsSet().getExpirationTime();
        }
        var verified = signedJWT.verify(verifier);
        if (!(verified && expiration_time.after(new Date()))) {
            throw new JOSEException("Token đã hết hạn");
        }
        return signedJWT;
    }

    public authentication_response authenticate_LoginOAuth2(register_account request) {
        Users user = Users.builder()
                .user_id(UUID.randomUUID().toString())
                .user_email(request.getEmail())
                .user_password(request.getPassword())
                .user_name(request.getName())
                .user_role(Users.Role.CUSTOMER)
                .build();

        user_repository.save(user);
        String token = generateToken(user, "sport_store.com");
        user_response userResponse = user_response.builder()
                .ID(user.getUser_id())
                .name(user.getUser_name())
                .gender(user.isUser_gender())
                .date_of_birth(user.getUser_date_of_birth())
                .phone(user.getUser_phone())
                .infoReceivers(user.getReceiver_Info())
                .email(user.getUser_email())
                .password(user.getUser_password())
                .role(user.getUser_role().toString())
                .build();

        return authentication_response.builder()
                .token(token)
                .user_response(userResponse)
                .build();
    }

    public authentication_response authenticate(authentication_request request, boolean loginOAuth2) throws Exception {
        Users user = user_repository.findByEmail(request.getEmail());
        if (user == null) {
            throw new Exception("Tài khoản không tồn tại");
        } else {
            if (!loginOAuth2) {
                boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getUser_password());
                if (!authenticated) {
                    throw new Exception("Mật khẩu không hợp lệ");
                }
            }
            String token = generateToken(user, "sport_store.com");
            user_response userResponse = user_response.builder()
                    .ID(user.getUser_id())
                    .name(user.getUser_name())
                    .gender(user.isUser_gender())
                    .date_of_birth(user.getUser_date_of_birth())
                    .phone(user.getUser_phone())
                    .infoReceivers(user.getReceiver_Info())
                    .email(user.getUser_email())
                    .password(user.getUser_password())
                    .role(user.getUser_role().toString())
                    .build();
            return authentication_response.builder()
                    .token(token)
                    .user_response(userResponse)
                    .build();
        }
    }

    public String generateToken(Users user, String issuer) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUser_email())
                .issuer(issuer)
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(validDuration, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", user.getUser_role().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String refreshToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(token, true);
        String email = signedJWT.getJWTClaimsSet().getSubject();
        Users user = user_service.getUserByEmail(email);

        Tokens tokensEntity = token_repository.findTokenByID(signedJWT.getJWTClaimsSet().getJWTID());
        token_repository.delete(tokensEntity);
        String new_token = generateToken(user, "sport_store.com");
        SignedJWT newSignedJWT = SignedJWT.parse(new_token);
        Tokens newTokensEntity = Tokens.builder()
                .token_id(newSignedJWT.getJWTClaimsSet().getJWTID())
                .token_expiration_time(LocalDateTime.now().plusHours(validDuration))
                .user_token(new_token)
                .user_email(email)
                .build();
        token_repository.save(newTokensEntity);
        return new_token;
    }

    public boolean isValidTokenRestPassword(String token_resetPassword) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(token_resetPassword, false);
        String email = signedJWT.getJWTClaimsSet().getSubject();
        Users user = user_service.getUserByEmail(email);
        return user != null && signedJWT.getJWTClaimsSet().getIssuer().equals("resetPassword");
    }
}
