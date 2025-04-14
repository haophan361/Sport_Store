package com.sport_store.Util;

import com.sport_store.DTO.response.account_Response.authentication_response;
import com.sport_store.Entity.Accounts;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Employees;
import com.sport_store.Service.account_Service;
import com.sport_store.Service.customer_Service;
import com.sport_store.Service.employee_Service;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadUser {
    private final customer_Service customer_service;
    private final account_Service account_service;
    private final employee_Service employee_service;

    public void CustomerSession(HttpSession session, authentication_response response) {
        Accounts account = account_service.getAccountByEmail(response.getAccount_response().getEmail());
        if (account != null) {
            if (account.getRole().toString().equals("CUSTOMER")) {
                Customers customer = customer_service.getUserByEmail(account.getEmail());
                session.setAttribute("name", customer.getCustomer_name());
            } else if (account.getRole().toString().equals("EMPLOYEE")) {
                Employees employee = employee_service.getEmployee(account.getEmail());
                session.setAttribute("name", employee.getEmployee_name());
            } else {
                session.setAttribute("name", "Quản trị viên");
            }
            session.setAttribute("email", account.getEmail());
            session.setAttribute("role", account.getRole().toString());
            session.setAttribute("isLoggedIn", 1);
        } else {
            session.setAttribute("isLoggedIn", 0);
        }
    }

    public void refreshUser(HttpSession session) {
        String email = session.getAttribute("email").toString();
        if (email != null) {
            Accounts account = account_service.getAccountByEmail(email);
            if (account != null) {
                if (account.getRole().toString().equals("CUSTOMER")) {
                    Customers customer = customer_service.getUserByEmail(email);
                    session.setAttribute("name", customer.getCustomer_name());
                }
            }
        }
    }

}
