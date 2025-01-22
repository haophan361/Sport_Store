package com.appliance_store.Util;

import com.appliance_store.DTO.response.authentication_response;
import com.appliance_store.Entity.User;
import com.appliance_store.Service.user_Service;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Load_userSession {
    private final user_Service user_service;

    public void userSession(HttpSession session, authentication_response response) {
        if (response != null) {
            User user = user_service.getUserByEmail(response.getUser_response().getEmail());
            if (user != null) {
                session.setAttribute("email", user.getUserEmail());
                session.setAttribute("name", user.getUserName());
                session.setAttribute("role", user.getUserRole());
                session.setAttribute("isLoggedIn", 1);
                session.setAttribute("token", response.getToken());
            } else {
                session.setAttribute("isLoggedIn", 0);
            }
        }
    }
}