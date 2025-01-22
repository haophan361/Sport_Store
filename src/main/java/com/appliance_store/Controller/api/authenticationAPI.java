package com.appliance_store.Controller.api;

import com.appliance_store.DTO.request.authentication_request;
import com.appliance_store.DTO.request.register_account;
import com.appliance_store.DTO.response.authentication_response;
import com.appliance_store.DTO.response.refreshToken_response;
import com.appliance_store.Entity.Token;
import com.appliance_store.Service.authentication_Service;
import com.appliance_store.Service.token_Service;
import com.appliance_store.Service.user_Service;
import com.appliance_store.Util.Load_userSession;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class authenticationAPI {
    @Value("${jwt.valid-duration}")
    private long validDuration;
    private final user_Service user_service;
    private final authentication_Service authentication_service;
    private final Load_userSession load_user_session;
    private final token_Service token_service;
    @PostMapping("/register")
    public ResponseEntity<String> Register(@RequestBody @Valid register_account request) {
        try {
            user_service.create_user(request);
            return ResponseEntity.ok("Bạn đã đăng kí thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody @Valid authentication_request request, HttpServletRequest httpServletRequest) throws Exception {
        authentication_response response=authentication_service.authenticate(request);
        HttpSession session = httpServletRequest.getSession(true);
        load_user_session.userSession(session,response);

        SignedJWT signedJWT= authentication_service.verifyToken(response.getToken(),false);
        Token tokenEntity=Token.builder()
                .tokenId(signedJWT.getJWTClaimsSet().getJWTID())
                .userToken(response.getToken())
                .tokenExpirationTime(LocalDateTime.now().plusHours(validDuration))
                .userEmail(request.getEmail())
                .build();
        token_service.createToken(tokenEntity);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION,"Bearer "+response.getToken())
                .body(response);
    }
    @PostMapping("/logout")
    public void Logout(@RequestHeader("Authorization") String tokenBearer, HttpServletRequest httpServletRequest) throws ParseException, JOSEException {
        String token=tokenBearer.substring(7);
        SignedJWT signedJWT=authentication_service.verifyToken(token,false);
        String tokenID=signedJWT.getJWTClaimsSet().getJWTID();
        Token token_localStorage=token_service.findTokenByID(tokenID);
        if (token_localStorage != null) {
            boolean success = token_service.deleteToken(token_localStorage);
            if (success) {
                HttpSession session = httpServletRequest.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
            }
        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<refreshToken_response> refreshToken(@RequestHeader("Authorization") String bearerToken) throws ParseException, JOSEException {
        String token=bearerToken.substring(7);
        String new_token=authentication_service.refreshToken(token);
        return ResponseEntity.ok(new refreshToken_response(new_token));
    }
}
