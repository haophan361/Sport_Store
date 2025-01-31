package com.sport_store.Util;

import com.sport_store.DTO.response.authentication_response;
import com.sport_store.Entity.Users;
import com.sport_store.Service.user_Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadUser {
    private final user_Service user_service;

    public void userSession(HttpSession session, authentication_response response) {
        if (response != null) {
            Users user = user_service.getUserByEmail(response.getUser_response().getEmail());
            if (user != null) {
                session.setAttribute("email", user.getUser_email());
                session.setAttribute("name", user.getUser_name());
                session.setAttribute("role", user.getUser_role());
                session.setAttribute("isLoggedIn", 1);
                session.setAttribute("token", response.getToken());
            } else {
                session.setAttribute("isLoggedIn", 0);
            }
        }
    }

    public void refreshUser(HttpSession session) {
        String email = session.getAttribute("email").toString();
        Users user = user_service.getUserByEmail(email);
        session.setAttribute("name", user.getUser_name());
        session.setAttribute("role", user.getUser_role());
    }


    public Cookie firstLogin_WithGoogle() {
        Cookie cookie = new Cookie("firstLogin_WithGoogle", "true");
        cookie.setPath("/");
        cookie.setMaxAge(1);
        return cookie;
    }
}
