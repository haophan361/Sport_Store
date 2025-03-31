package com.sport_store.Util;

import com.sport_store.DTO.response.authentication_response;
import com.sport_store.Entity.Accounts;
import com.sport_store.Entity.Customers;
import com.sport_store.Service.account_Service;
import com.sport_store.Service.customer_Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadUser {
    private final customer_Service customer_service;
    private final account_Service account_service;

    public void CustomerSession(HttpSession session, authentication_response response) {
        if (response != null) {
            Customers customer = customer_service.getUserByEmail(response.getAccount_response().getEmail());
            if (customer != null) {
                session.setAttribute("email", customer.getCustomer_email());
                session.setAttribute("name", customer.getCustomer_name());
                session.setAttribute("isLoggedIn", 1);
            } else {
                Accounts account = account_service.getAccountByEmail(response.getAccount_response().getEmail());
                if (account != null) {
                    session.setAttribute("email", account.getEmail());
                    session.setAttribute("name", "Quản trị viên");
                    session.setAttribute("isLoggedIn", 1);
                } else {
                    session.setAttribute("isLoggedIn", 0);
                }
            }
        }
    }

    public void refreshUser(HttpSession session) {
        String email = session.getAttribute("email").toString();
        Customers customer = customer_service.getUserByEmail(email);
        session.setAttribute("name", customer.getCustomer_name());
    }


    public Cookie firstLogin_WithGoogle() {
        Cookie cookie = new Cookie("firstLogin_WithGoogle", "true");
        cookie.setPath("/");
        cookie.setMaxAge(1);
        return cookie;
    }
}
