package com.sport_store.Controller.api;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.sport_store.DTO.request.UserDTO.changePassword_request;
import com.sport_store.DTO.request.UserDTO.forgetPassword_request;
import com.sport_store.DTO.request.UserDTO.updateUser_request;
import com.sport_store.Entity.Users;
import com.sport_store.Service.authentication_Service;
import com.sport_store.Service.cookie_Service;
import com.sport_store.Service.mail_Service;
import com.sport_store.Service.user_Service;
import com.sport_store.Util.LoadUser;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class userAPI {
    private final user_Service user_service;
    private final LoadUser load_user_session;
    private final mail_Service mail_service;
    private final authentication_Service authentication_service;
    private final PasswordEncoder passwordEncoder;
    private final cookie_Service cookie_service;

    @PostMapping("/user/changeInfoUser")
    public ResponseEntity<String> changeInfoUser(@RequestBody updateUser_request request, HttpServletRequest httpServletRequest) {
        user_service.updateUser(request);
        load_user_session.refreshUser(httpServletRequest.getSession());
        return ResponseEntity.ok("Cập nhật thông tin người dùng thành công");
    }

    @PostMapping("/web/sendCode_VerifyEmail_forgetPassword")
    public ResponseEntity<String> sendCode_verifyEmail_forgetPassword(@RequestParam String email, HttpServletResponse httpServletResponse) {
        Users user = user_service.getUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("Tài khoản sử dụng Email này hiện chưa có trên hệ thống");
        }
        String code_verifyEmail = UUID.randomUUID().toString().substring(0, 6);
        String token_verifyEmail = authentication_service.generateToken(user, code_verifyEmail);
        httpServletResponse.addCookie(cookie_service.create_verfityEmailCookie(token_verifyEmail));
        try {
            mail_service.Send_codeVerifyEmail(user.getUser_email(), code_verifyEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Hãy kiểm tra Email: " + email + " để lấy mã xác nhận");
    }

    @PostMapping("/web/resendCode_VerifyEmail")
    public ResponseEntity<String> ResendCode_verifyEmail(@CookieValue(value = "token_verifyEmail") String token_verifyEmail, @RequestBody String typeVerify, HttpServletResponse httpServletResponse) {
        try {
            SignedJWT signedJWT = authentication_service.verifyToken(token_verifyEmail, false);
            String email = signedJWT.getJWTClaimsSet().getSubject();
            Users user;
            if (typeVerify.equals("forgetPassword")) {
                user = user_service.getUserByEmail(email);
            } else {
                user = Users
                        .builder()
                        .user_email(email)
                        .user_role(Users.Role.EMPLOYEE)
                        .build();
            }

            String code_verifyEmail = UUID.randomUUID().toString().substring(0, 6);
            String new_token_verifyEmail = authentication_service.generateToken(user, code_verifyEmail);
            httpServletResponse.addCookie(cookie_service.create_verfityEmailCookie(new_token_verifyEmail));
            mail_service.Send_codeVerifyEmail(user.getUser_email(), code_verifyEmail);
        } catch (JOSEException | ParseException | MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Đã gửi lại mã xác nhận thành công");
    }

    @PostMapping("/web/checkCode_ForgetPassword")
    public ResponseEntity<Map<String, String>> checkCode_ForgetPassword(@CookieValue(value = "token_verifyEmail") String token_verifyEmail, @RequestBody String code, HttpServletResponse httpServletResponse) {
        try {
            SignedJWT signedJWT = authentication_service.verifyToken(token_verifyEmail, false);
            String issuer = signedJWT.getJWTClaimsSet().getIssuer();
            Users user = user_service.getUserByEmail(signedJWT.getJWTClaimsSet().getSubject());
            Map<String, String> response = new HashMap<>();
            if (issuer.equals(code)) {
                String token_resetPassword = authentication_service.generateToken(user, "reset_password");
                httpServletResponse.addCookie(cookie_service.create_ResetPasswordCookie(token_resetPassword));
                response.put("message", "Xác nhận thành công");
                response.put("redirectUrl", "/form/forgetPassword");
                return ResponseEntity.ok(response);
            } else {
                throw new RuntimeException("Mã xác nhận sai vui lòng nhập lại");
            }
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/web/checkCode_Register")
    public ResponseEntity<Map<String, String>> checkCode_Register(@CookieValue(value = "token_verifyEmail") String token_verifyEmail, @RequestBody String code) {
        try {
            SignedJWT signedJWT = authentication_service.verifyToken(token_verifyEmail, false);
            String issuer = signedJWT.getJWTClaimsSet().getIssuer();
            Map<String, String> response = new HashMap<>();
            if (issuer.equals(code)) {
                response.put("message", "Xác nhận thành công");
                return ResponseEntity.ok(response);
            } else {
                throw new RuntimeException("Mã xác nhận sai vui lòng nhập lại");
            }
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/web/forgetPassword")
    public ResponseEntity<String> forgetPassword(@CookieValue(value = "token_resetPassword") String token_resetPassword, @RequestBody forgetPassword_request request) {
        try {
            SignedJWT signedJWT = authentication_service.verifyToken(token_resetPassword, false);
            Users user = user_service.getUserByEmail(signedJWT.getJWTClaimsSet().getSubject());
            if (!request.getNew_password().equals(request.getConfirm_password())) {
                throw new RuntimeException("Mật khẩu mới và xác nhận mật khẩu không trùng khớp");
            }
            user_service.changePassword(user, request.getNew_password());
            return ResponseEntity.ok("Cập nhật mật khẩu mới thành công");
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/user/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody changePassword_request request) {
        Users user = user_service.get_myInfo();
        if (!passwordEncoder.matches(request.getOld_password(), user.getUser_password())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác");
        }
        if (!request.getNew_password().equals(request.getConfirm_password())) {
            throw new RuntimeException("Mật khẩu mới và xác nhận mật khẩu không trùng khớp");
        }
        user_service.changePassword(user, request.getNew_password());
        return ResponseEntity.ok("Cập nhật mật khẩu mới thành công");
    }
}
