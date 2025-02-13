package com.sport_store.Service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class cookie_Service {
    public Cookie create_tokenCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
        return cookie;
    }

    public Cookie create_verfityEmailCookie(String token_verifyEmail) {
        Cookie cookie = new Cookie("token_verifyEmail", token_verifyEmail);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(600);
        return cookie;
    }

    public Cookie create_ResetPasswordCookie(String token_resetPassword) {
        Cookie cookie = new Cookie("token_resetPassword", token_resetPassword);
        cookie.setPath("/web/forgetPassword");
        cookie.setMaxAge(300);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
