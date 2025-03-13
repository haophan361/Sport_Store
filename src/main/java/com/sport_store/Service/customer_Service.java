package com.sport_store.Service;

import com.sport_store.DTO.request.UserDTO.updateCustomer_request;
import com.sport_store.Entity.Customers;
import com.sport_store.Repository.customer_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class customer_Service {
    private final customer_Repository customer_repository;

    public void create_customer(Customers customer) {

        if (customer_repository.findByEmail(customer.getCustomer_email()) != null) {
            throw new RuntimeException("Email đã tồn tại");
        }
        if (customer_repository.findByPhone(customer.getCustomer_phone()) != null) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }
        customer_repository.save(customer);
    }


    public Customers get_myInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        return customer_repository.findByEmail(email);
    }

    public Customers getUserByEmail(String email) {
        return customer_repository.findByEmail(email);
    }

    public void updateCustomer(updateCustomer_request request) {
        Customers customer = get_myInfo();
        customer.setCustomer_name(request.getCustomer_name());
        customer.setCustomer_phone(request.getCustomer_phone());
        customer.setCustomer_date_of_birth(request.getCustomer_date_of_birth());
        customer_repository.save(customer);
    }

    public boolean existByEmail(String email) {
        return customer_repository.findByEmail(email) != null;
    }
}
