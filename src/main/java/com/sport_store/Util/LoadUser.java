package com.sport_store.Util;

import com.sport_store.DTO.response.account_Response.authentication_response;
import com.sport_store.Entity.Accounts;
import com.sport_store.Entity.Carts;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Employees;
import com.sport_store.Service.account_Service;
import com.sport_store.Service.cart_Service;
import com.sport_store.Service.customer_Service;
import com.sport_store.Service.employee_Service;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LoadUser {
    private final customer_Service customer_service;
    private final account_Service account_service;
    private final employee_Service employee_service;
    private final cart_Service cart_service;

    public void userSession(HttpSession session, authentication_response response) {
        Accounts account = account_service.getAccountByEmail(response.getAccount_response().getEmail());
        if (account != null) {
            if (account.getRole().toString().equals("CUSTOMER")) {
                Customers customer = customer_service.getUserByEmail(account.getEmail());
                session.setAttribute("cart_size", cart_service.findAllCartsByCustomers(customer).size());
                session.setAttribute("name", customer.getCustomer_name());
                session.setAttribute("customerId", customer.getCustomer_id());
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
        String email = (String) session.getAttribute("email");
        if (email != null) {
            Accounts account = account_service.getAccountByEmail(email);
            if (account != null && account.getRole().toString().equals("CUSTOMER")) {
                Customers customer = customer_service.getUserByEmail(email);
                session.setAttribute("name", customer.getCustomer_name());
            }
            if (account != null && account.getRole().toString().equals("EMPLOYEE")) {
                Employees employee = employee_service.getEmployee(email);
                session.setAttribute("name", employee.getEmployee_name());
            }
        }
    }

    public void refreshCart(HttpSession session) {
        String email = (String) session.getAttribute("email");
        Customers customer = customer_service.getUserByEmail(email);
        if (customer != null) {
            List<Carts> carts = cart_service.findAllCartsByCustomers(customer);
            session.setAttribute("cart_size", carts.size());
        }
    }
}