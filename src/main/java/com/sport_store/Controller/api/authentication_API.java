package com.sport_store.Controller.api;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.sport_store.DTO.request.AuthenticationDTO.authentication_request;
import com.sport_store.DTO.request.UserDTO.register_account;
import com.sport_store.DTO.response.authentication_response;
import com.sport_store.DTO.response.refreshToken_response;
import com.sport_store.Entity.Accounts;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Tokens;
import com.sport_store.Service.*;
import com.sport_store.Util.LoadUser;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class authentication_API {
    @Value("${jwt.valid-duration}")
    private long validDuration;
    private final customer_Service customer_service;
    private final authentication_Service authentication_service;
    private final LoadUser loadUser;
    private final token_Service token_service;
    private final cookie_Service cookie_service;
    private final mail_Service mail_service;
    private final PasswordEncoder passwordEncoder;
    private final account_Service account_service;

    @PostMapping("/web/sendCode_VerifyEmail_Register")
    public ResponseEntity<?> sendCode_verifyEmail_Register(@RequestBody @Valid register_account request,
                                                           HttpServletResponse httpServletResponse, HttpSession httpSession) {
        Customers customer = Customers
                .builder()
                .customer_id(UUID.randomUUID().toString())
                .customer_name(request.getName())
                .customer_date_of_birth(request.getDate_of_birth())
                .customer_phone(request.getPhone())
                .customer_email(request.getEmail())
                .is_active(true)
                .build();
        Accounts account = Accounts
                .builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Accounts.Role.CUSTOMER)
                .is_active(true)
                .build();
        httpSession.setAttribute("customer", customer);
        httpSession.setAttribute("account", account);
        httpSession.setAttribute("email", request.getEmail());
        String code_verifyEmail = UUID.randomUUID().toString().substring(0, 6);
        String token_verifyEmail = authentication_service.generateToken(account, code_verifyEmail);
        Cookie cookie_verifyEmail = cookie_service.create_tokenCookie(token_verifyEmail, "token_verifyEmail",
                "/web/checkCode_Register", 300, true);
        httpServletResponse.addCookie(cookie_verifyEmail);
        try {
            mail_service.Send_codeVerifyEmail(account.getEmail(), code_verifyEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Hãy kiểm tra Email: " + request.getEmail() + " để lấy mã xác nhận"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> Register(HttpSession httpSession) {
        Customers customer = (Customers) httpSession.getAttribute("customer");
        Accounts accounts = (Accounts) httpSession.getAttribute("account");
        customer_service.create_customer(customer);
        account_service.create_account(accounts);
        httpSession.removeAttribute("customer");
        httpSession.removeAttribute("account");
        httpSession.removeAttribute("email");
        return ResponseEntity.ok(Collections.singletonMap("message", "Bạn đã đăng ký thành công"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody @Valid authentication_request request, HttpServletRequest httpServletRequest,
                                   HttpServletResponse httpServletResponse) throws Exception {
        authentication_response response = authentication_service.authenticate(request, false);
        HttpSession session = httpServletRequest.getSession();
        loadUser.CustomerSession(session, response);

        SignedJWT signedJWT = authentication_service.verifyToken(response.getToken(), false);
        Tokens tokensEntity = Tokens.builder()
                .token_id(signedJWT.getJWTClaimsSet().getJWTID())
                .user_token(response.getToken())
                .token_expiration_time(LocalDateTime.now().plusHours(validDuration))
                .user_email(request.getEmail())
                .build();
        token_service.createToken(tokensEntity);
        Cookie cookie = cookie_service.create_tokenCookie(response.getToken(), "token", "/", 3600, true);
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(Collections.singletonMap("message", "Đăng nhập thành công"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> Logout(@CookieValue(value = "token") String token, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ParseException, JOSEException {
        SignedJWT signedJWT = authentication_service.verifyToken(token, false);
        String tokenID = signedJWT.getJWTClaimsSet().getJWTID();
        Tokens tokens_localStorage = token_service.findTokenByID(tokenID);
        if (tokens_localStorage != null) {
            boolean success = token_service.deleteToken(tokens_localStorage);
            if (success) {
                HttpSession session = httpServletRequest.getSession(false);
                if (session != null) {
                    session.invalidate();
                    Cookie cookie = new Cookie("token", null);
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    httpServletResponse.addCookie(cookie);
                    return ResponseEntity.ok(Collections.singletonMap("message", "Đăng xuất thành công"));
                } else {
                    throw new RuntimeException("Phiên đăng nhập đã hết hạn");
                }
            } else {
                throw new RuntimeException("Token đã hết hạn");
            }
        } else {
            throw new RuntimeException("Không tìm thấy token");
        }
    }

    @GetMapping("/check_login")
    public ResponseEntity<Boolean> checkLoginStatus(@CookieValue(value = "token", required = false) String token) {
        try {
            if (token == null) {
                return ResponseEntity.ok(false);
            }
            authentication_service.verifyToken(token, false);
            return ResponseEntity.ok(true);
        } catch (ParseException | JOSEException e) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<refreshToken_response> refreshToken(@CookieValue("token") String token, HttpServletResponse httpServletResponse) throws ParseException, JOSEException {
        String new_token = authentication_service.refreshToken(token);
        Cookie new_cookie = cookie_service.create_tokenCookie(new_token, "token", "/", 3600, true);
        httpServletResponse.addCookie(new_cookie);
        return ResponseEntity.ok(new refreshToken_response(new_token));
    }
}
