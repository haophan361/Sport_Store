package com.sport_store.Service;

import com.sport_store.DTO.request.customer_Request.updateCustomer_request;
import com.sport_store.DTO.request.employee_request.create_employee;
import com.sport_store.DTO.request.employee_request.update_employee;
import com.sport_store.DTO.response.employee_response.employeeInfo_response;
import com.sport_store.Entity.Accounts;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Employees;
import com.sport_store.Repository.account_Repository;
import com.sport_store.Repository.employee_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class employee_Service {
    private final employee_Repository employee_repository;
    private final account_Repository account_repository;
    private final account_Service account_service;

    public Employees getEmployee(String email) {
        return employee_repository.getEmployeeByEmail(email);
    }

    public List<employeeInfo_response> getEmployeeInfoList() {
        List<Employees> employees = employee_repository.findAll();
        return getEmployeeInfoResponses(employees);
    }

    public List<employeeInfo_response> getEmployeeInfoResponses(List<Employees> employees) {
        return employees.stream()
                .map(employee -> {
                    Accounts account = account_repository.findById(employee.getEmployee_email())
                            .orElse(null);

                    employeeInfo_response dto = new employeeInfo_response();
                    dto.setName(employee.getEmployee_name());
                    dto.setEmail(employee.getEmployee_email());
                    dto.setPhone(employee.getEmployee_phone());
                    dto.setDateOfBirth(employee.getEmployee_date_of_birth());
                    dto.setAddress(employee.getEmployee_address());
                    dto.setGender(employee.isEmployee_gender());
                    dto.setOnline(account != null && account.is_online());
                    dto.setActive(account != null && account.is_active());
                    return dto;
                })
                .sorted(Comparator
                        .comparing(employeeInfo_response::isActive, Comparator.reverseOrder())
                        .thenComparing(employeeInfo_response::isOnline, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public boolean createEmployee(create_employee request) {
        // Check if email already exists
        LocalDate dob = request.getEmployee_date_of_birth();
        int age = Period.between(dob, LocalDate.now()).getYears();
        if (age < 18) {
            throw new RuntimeException("Nhân viên không được dưới 18 tuổi.");
        }

        // Check if email or phone already exists
        if (employee_repository.existsByEmployeeEmail(request.getEmployee_email()) ||
                employee_repository.existsByEmployeePhone(request.getEmployee_phone()) ||
                account_repository.existsByEmail(request.getEmployee_email())) {
            throw new RuntimeException("Email hoặc số điện thoại đã được sử dụng.");
        }

        // Generate unique employee ID (e.g., UUID or custom logic)
        String employeeId = UUID.randomUUID().toString();

        // Create Employees entity
        Employees employee = Employees.builder()
                .employee_id(employeeId)
                .employee_name(request.getEmployee_name())
                .employee_address(request.getEmployee_address())
                .employee_phone(request.getEmployee_phone())
                .employee_email(request.getEmployee_email())
                .employee_date_of_birth(request.getEmployee_date_of_birth())
                .employee_gender(request.getEmployee_gender())
                .build();

        // Create Accounts entity with encrypted password
        Accounts account = Accounts.builder()
                .email(request.getEmployee_email())
                .password(request.getPassword())
                .is_active(true) // New account is active by default
                .is_online(false) // Not online initially
                .role(Accounts.Role.EMPLOYEE)
                .build();

        // Save both entities
        employee_repository.save(employee);
        account_service.create_account(account);
        return true;
    }

    public Employees get_myInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        return employee_repository.getEmployeeByEmail(email);
    }

    public void updateEmployee(update_employee request) {
        Employees employee = get_myInfo();
        if (employee == null) {
            throw new RuntimeException("Không tìm thấy nhân viên.");
        }
        if (employee_repository.existsByEmployeePhone(request.getEmployee_phone())) {
            throw new RuntimeException("Số điện thoại đã được sử dụng bởi nhân viên khác.");
        }
        employee.setEmployee_address(request.getEmployee_address());
        employee.setEmployee_phone(request.getEmployee_phone());
        employee_repository.save(employee);
    }
}
