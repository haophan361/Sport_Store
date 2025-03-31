package com.sport_store.Service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sport_store.DTO.request.AuthenticationDTO.authentication_request;
import com.sport_store.DTO.request.UserDTO.register_account;
import com.sport_store.DTO.response.account_response;
import com.sport_store.DTO.response.authentication_response;
import com.sport_store.Entity.Accounts;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Tokens;
import com.sport_store.Repository.account_Repository;
import com.sport_store.Repository.customer_Repository;
import com.sport_store.Repository.token_Repository;
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
    @Value("${valid-token.resetPassword}")
    private long validResetPasswordDuration;
    private final customer_Repository customer_repository;
    private final PasswordEncoder passwordEncoder;
    private final account_Service account_service;
    private final token_Repository token_repository;
    private final account_Repository account_repository;

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
        Customers customer = Customers
                .builder()
                .customer_id(UUID.randomUUID().toString())
                .customer_email(request.getEmail())
                .customer_name(request.getName())
                .build();
        customer_repository.save(customer);

        Accounts account = Accounts
                .builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Accounts.Role.valueOf(request.getRole()))
                .build();
        account_repository.save(account);

        String token = generateToken(account, "sport_store.com");
        account_response userResponse = account_response.builder()
                .email(account.getEmail())
                .password(account.getPassword())
                .role(account.getRole().toString())
                .active(account.is_active())
                .build();

        return authentication_response.builder()
                .token(token)
                .account_response(userResponse)
                .build();
    }

    public authentication_response authenticate(authentication_request request, boolean loginOAuth2) throws Exception {
        Accounts account = account_service.getAccountByEmail(request.getEmail());
        if (account == null) {
            throw new Exception("Tài khoản không tồn tại");
        } else {
            if (!loginOAuth2) {
                boolean authenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());
                if (!authenticated) {
                    throw new Exception("Mật khẩu không hợp lệ");
                }
            }
            String token = generateToken(account, "sport_store.com");
            account_response accountResponse = account_response
                    .builder()
                    .email(account.getEmail())
                    .password(account.getPassword())
                    .role(account.getRole().toString())
                    .active(account.is_active())
                    .build();

            return authentication_response.builder()
                    .token(token)
                    .account_response(accountResponse)
                    .build();
        }
    }

    public String generateToken(Accounts account, String issuer) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        Date expirationTime;
        if (issuer.equals("sport_store.com")) {
            expirationTime = new Date(Instant.now().plus(validDuration, ChronoUnit.HOURS).toEpochMilli());
        } else {
            expirationTime = new Date(Instant.now().plus(validResetPasswordDuration, ChronoUnit.MINUTES).toEpochMilli());
        }
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .issuer(issuer)
                .issueTime(new Date())
                .expirationTime(expirationTime)
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", account.getRole().toString())
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
        Accounts account = account_service.getAccountByEmail(email);

        Tokens tokensEntity = token_repository.findTokenByID(signedJWT.getJWTClaimsSet().getJWTID());
        token_repository.delete(tokensEntity);
        String new_token = generateToken(account, "sport_store.com");
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

}
