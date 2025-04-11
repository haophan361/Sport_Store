package com.sport_store.Service;

import com.sport_store.Entity.Employees;
import com.sport_store.Repository.employee_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class employee_Service {
    private final employee_Repository employee_repository;

    public Employees getEmployee(String email) {
        return employee_repository.getEmployeeByEmail(email);
    }
}
