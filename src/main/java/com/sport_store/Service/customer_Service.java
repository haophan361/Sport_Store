package com.sport_store.Service;

import com.sport_store.DTO.request.customer_Request.updateCustomer_request;
import com.sport_store.DTO.response.customer_response.customerInfo_response;
import com.sport_store.Entity.Accounts;
import com.sport_store.Entity.Customers;
import com.sport_store.Repository.account_Repository;
import com.sport_store.Repository.customer_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class customer_Service {
    private final customer_Repository customer_repository;
    private final account_Repository account_repository;

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
        if (customer_repository.findByPhone(customer.getCustomer_phone()) != null) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }
        customer.setCustomer_phone(request.getCustomer_phone());
        customer.setCustomer_date_of_birth(request.getCustomer_date_of_birth());
        customer_repository.save(customer);
    }

    public boolean existByEmail(String email) {
        return customer_repository.findByEmail(email) != null;
    }

    public List<customerInfo_response> getCustomerInfoList() {
        List<Customers> customers = customer_repository.findAll();
        return getCustomerInfoResponses(customers);
    }

    public List<customerInfo_response> getCustomerInfoResponses(List<Customers> customers) {
        return customers.stream()
                .map(customer -> {
                    Accounts account = account_repository.findById(customer.getCustomer_email())
                            .orElse(null);

                    customerInfo_response dto = new customerInfo_response();
                    dto.setName(customer.getCustomer_name());
                    dto.setEmail(customer.getCustomer_email());
                    dto.setPhone(customer.getCustomer_phone());
                    dto.setDateOfBirth(customer.getCustomer_date_of_birth());
                    dto.setOnline(account != null && account.is_online());
                    dto.setActive(account != null && account.is_active());
                    return dto;
                })
                .sorted(Comparator
                        .comparing(customerInfo_response::isActive, Comparator.reverseOrder())
                        .thenComparing(customerInfo_response::isOnline, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public Customers finbyId(String id){
        return customer_repository.findById(id).orElse(null);
    }
}
