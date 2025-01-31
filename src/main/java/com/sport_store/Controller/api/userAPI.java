package com.sport_store.Controller.api;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.sport_store.DTO.request.UserDTO.changePassword_request;
import com.sport_store.DTO.request.UserDTO.forgetPassword_request;
import com.sport_store.DTO.request.UserDTO.updateUser_request;
import com.sport_store.Entity.Users;
import com.sport_store.Service.authentication_Service;
import com.sport_store.Service.mail_Service;
import com.sport_store.Service.user_Service;
import com.sport_store.Util.LoadUser;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class userAPI {
    private final user_Service user_service;
    private final LoadUser load_user_session;
    private final mail_Service mail_service;
    private final authentication_Service authentication_service;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/user/changeInfoUser")
    public ResponseEntity<String> changeInfoUser(@RequestBody updateUser_request request, HttpServletRequest httpServletRequest) {
        user_service.updateUser(request);
        load_user_session.refreshUser(httpServletRequest.getSession());
        return ResponseEntity.ok("Cập nhật thông tin người dùng thành công");
    }

    @GetMapping("/web/sendLink_ForgetPassword")
    public ResponseEntity<String> sendLinkForgetPassword(@RequestParam String email) throws MessagingException {
        Users user = user_service.getUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("Tài khoản sử dụng Email này hiện chưa có trên hệ thống");
        }
        String token_resetPassword = authentication_service.generateToken(user, "resetPassword");
        String resetPasswordLink = "http://localhost:8080/web/setToken_forgetPassword?token_resetPassword=" + token_resetPassword;
        mail_service.SendEmailForgotPassword(user.getUser_email(), resetPasswordLink);
        return ResponseEntity.ok("Hãy kiểm tra Email: " + email + " để lấy đường link và reset mật khẩu");
    }

    @GetMapping("/web/check_token_resetPassword")
    public ResponseEntity<Void> getForm_changePassword(@CookieValue(value = "token_resetPassword") String token_resetPassword) throws ParseException, JOSEException {
        if (authentication_service.isValidTokenRestPassword(token_resetPassword)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
