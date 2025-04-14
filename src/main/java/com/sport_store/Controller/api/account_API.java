package com.sport_store.Controller.api;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.sport_store.DTO.request.account_Request.changePassword_request;
import com.sport_store.DTO.request.account_Request.forgetPassword_request;
import com.sport_store.Entity.Accounts;
import com.sport_store.Service.account_Service;
import com.sport_store.Service.authentication_Service;
import com.sport_store.Service.cookie_Service;
import com.sport_store.Service.mail_Service;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class account_API {
    private final account_Service account_service;
    private final cookie_Service cookie_service;
    private final mail_Service mail_service;
    private final authentication_Service authentication_service;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/web/sendCode_VerifyEmail_forgetPassword")
    public ResponseEntity<?> sendCode_verifyEmail_forgetPassword(@RequestBody String email, HttpSession session, HttpServletResponse httpServletResponse) {
        Accounts account = account_service.getAccountByEmail(email);
        if (account == null) {
            throw new RuntimeException("Tài khoản sử dụng Email này hiện chưa có trên hệ thống");
        }
        String code_verifyEmail = UUID.randomUUID().toString().substring(0, 6);
        String token_verifyEmail = authentication_service.generateToken(account, code_verifyEmail);
        Cookie cookie_verifyEmail = cookie_service.create_Cookie(token_verifyEmail, "token_verifyEmail",
                "/web/checkCode_ForgetPassword", 300, true);
        httpServletResponse.addCookie(cookie_verifyEmail);
        session.setAttribute("email", email);
        try {
            mail_service.Send_codeVerifyEmail(account.getEmail(), code_verifyEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Hãy kiểm tra Email: " + email + " để lấy mã xác nhận"));
    }

    @PostMapping("/web/resendCode_VerifyEmail")
    public ResponseEntity<?> ResendCode_verifyEmail(HttpSession session, @RequestBody String typeVerify, HttpServletResponse httpServletResponse) {
        String email = (String) session.getAttribute("email");
        Accounts account;
        if (typeVerify.equals("forgetPassword")) {
            account = account_service.getAccountByEmail(email);
        } else {
            account = Accounts
                    .builder()
                    .email(email)
                    .role(Accounts.Role.EMPLOYEE)
                    .build();
        }
        String code_verifyEmail = UUID.randomUUID().toString().substring(0, 6);
        String new_token_verifyEmail = authentication_service.generateToken(account, code_verifyEmail);
        String path = (typeVerify.equals("forgetPassword")) ? "/web/checkCode_ForgetPassword" : "/web/checkCode_Register";
        Cookie cookie_verifyEmail = cookie_service.create_Cookie(new_token_verifyEmail, "token_verifyEmail",
                path, 300, true);
        httpServletResponse.addCookie(cookie_verifyEmail);
        try {
            mail_service.Send_codeVerifyEmail(account.getEmail(), code_verifyEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi mã xác nhận tất bại");
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Đã gửi lại mã xác nhận hãy kiểm tra lại"));
    }

    @PostMapping("/web/checkCode_ForgetPassword")
    public ResponseEntity<?> checkCode_ForgetPassword(@CookieValue(value = "token_verifyEmail") String token_verifyEmail, @RequestBody String code, HttpServletResponse httpServletResponse) {
        try {
            SignedJWT signedJWT = authentication_service.verifyToken(token_verifyEmail, false);
            String issuer = signedJWT.getJWTClaimsSet().getIssuer();
            Accounts account = account_service.getAccountByEmail(signedJWT.getJWTClaimsSet().getSubject());
            if (issuer.equals(code)) {
                String token_resetPassword = authentication_service.generateToken(account, "reset_password");
                Cookie cookie_forgetPassword = cookie_service.create_Cookie(token_resetPassword, "token_resetPassword",
                        "/web/forgetPassword", 300, true);
                Cookie deleteCookie = cookie_service.deleteCookie("token_verifyEmail", "/web/checkCode_ForgetPassword");
                httpServletResponse.addCookie(cookie_forgetPassword);
                httpServletResponse.addCookie(deleteCookie);
                return ResponseEntity.ok(Collections.singletonMap("message", "Xác nhận thành công"));
            } else {
                throw new RuntimeException("Mã xác nhận sai vui lòng nhập lại");
            }
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/web/forgetPassword")
    public ResponseEntity<?> forgetPassword(@CookieValue(value = "token_resetPassword") String token_resetPassword, @RequestBody forgetPassword_request request, HttpServletResponse httpServletResponse) {
        try {
            SignedJWT signedJWT = authentication_service.verifyToken(token_resetPassword, false);
            Accounts accounts = account_service.getAccountByEmail(signedJWT.getJWTClaimsSet().getSubject());
            if (!request.getNew_password().equals(request.getConfirm_password())) {
                throw new RuntimeException("Mật khẩu mới và xác nhận mật khẩu không trùng khớp");
            }
            account_service.changePassword(accounts, request.getNew_password());
            Cookie deleteCookie = cookie_service.deleteCookie("token_resetPassword", "/web/forgetPassword");
            httpServletResponse.addCookie(deleteCookie);
            return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật mật khẩu mới thành công"));
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/user/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody changePassword_request request) {
        Accounts account = account_service.get_myAccountInfo();
        if (!passwordEncoder.matches(request.getOld_password(), account.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác");
        }
        if (!request.getNew_password().equals(request.getConfirm_password())) {
            throw new RuntimeException("Mật khẩu mới và xác nhận mật khẩu không trùng khớp");
        }
        account_service.changePassword(account, request.getNew_password());
        return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật mật khẩu mới thành công"));
    }
}
