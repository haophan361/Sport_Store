package com.sport_store.Service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class cookie_Service {
    public Cookie create_tokenCookie(String token_value, String token_name, String path, int maxAge, boolean isHttpOnly) {
        Cookie cookie = new Cookie(token_name, token_value);
        cookie.setPath(path);
        cookie.setHttpOnly(isHttpOnly);
        cookie.setMaxAge(maxAge);
        return cookie;
    }
    

    public Cookie deleteCookie(String cookie_name, String path) {
        Cookie cookie = new Cookie(cookie_name, "");
        cookie.setPath(path);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
