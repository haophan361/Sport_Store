package com.sport_store.Controller.api;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.sport_store.DTO.request.customer_Request.updateCustomer_request;
import com.sport_store.Service.authentication_Service;
import com.sport_store.Service.cookie_Service;
import com.sport_store.Service.customer_Service;
import com.sport_store.Service.mail_Service;
import com.sport_store.Util.LoadUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class customer_API {
    private final customer_Service customer_service;
    private final LoadUser load_user_session;
    private final mail_Service mail_service;
    private final authentication_Service authentication_service;
    private final cookie_Service cookie_service;

    @PostMapping("/customer/changeInfoCustomer")
    public ResponseEntity<?> changeInfoUser(@RequestBody updateCustomer_request request, HttpServletRequest httpServletRequest) {
        customer_service.updateCustomer(request);
        load_user_session.refreshUser(httpServletRequest.getSession());
        return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thông tin người dùng thành công"));
    }


    @PostMapping("/web/checkCode_Register")
    public ResponseEntity<?> checkCode_Register(@CookieValue(value = "token_verifyEmail") String token_verifyEmail, @RequestBody String code, HttpServletResponse httpServletResponse) {
        try {
            SignedJWT signedJWT = authentication_service.verifyToken(token_verifyEmail, false);
            String issuer = signedJWT.getJWTClaimsSet().getIssuer();
            if (issuer.equals(code)) {
                Cookie deleteCookie = cookie_service.deleteCookie("token_verifyEmail", "/web/checkCode_Register");
                httpServletResponse.addCookie(deleteCookie);
                return ResponseEntity.ok(Collections.singletonMap("message", "Xác nhận thành công"));
            } else {
                throw new RuntimeException("Mã xác nhận sai vui lòng nhập lại");
            }
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
