package com.sport_store.Repository;

import com.sport_store.Entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface employee_Repository extends JpaRepository<Employees, String> {
    @Query("SELECT e  FROM Employees e WHERE e.employee_email= :email")
    public Employees getEmployeeByEmail(String email);

    @Query("SELECT count(e)>0 FROM Employees e WHERE e.employee_email = :email")
    boolean existsByEmployeeEmail(String email);

    @Query("SELECT count(e)>0 FROM Employees e WHERE e.employee_phone = :phone")
    boolean existsByEmployeePhone(String phone);
}
