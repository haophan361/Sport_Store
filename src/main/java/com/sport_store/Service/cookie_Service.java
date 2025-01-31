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

    public Cookie create_tokenResetPasswordCookie(String token_resetPassword) {
        Cookie cookie = new Cookie("token_resetPassword", token_resetPassword);
        cookie.setPath("/");
        cookie.setMaxAge(900);
        return cookie;
    }
}
