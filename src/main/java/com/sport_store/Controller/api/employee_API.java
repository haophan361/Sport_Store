package com.sport_store.Controller.api;

import com.sport_store.DTO.request.employee_request.create_employee;
import com.sport_store.DTO.request.employee_request.update_employee;
import com.sport_store.DTO.response.employee_response.employeeInfo_response;
import com.sport_store.Service.authentication_Service;
import com.sport_store.Service.cookie_Service;
import com.sport_store.Service.employee_Service;
import com.sport_store.Util.LoadUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class employee_API {
    private final employee_Service employee_service;
    private final LoadUser load_user_session;
    private final authentication_Service authentication_service;
    private final cookie_Service cookie_service;
    private final com.sport_store.Service.customer_Service customer_Service;

    @GetMapping("/admin/employees")
    public ResponseEntity<?> getEmployeeInfoList() {
        try {
            List<employeeInfo_response> employees = employee_service.getEmployeeInfoList();
            if (employees.isEmpty()) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Danh sách nhân viên trống"));
            }
            return ResponseEntity.ok(employees);
        } catch (RuntimeException e) {
            throw new RuntimeException("Không lấy được danh sách nhân viên" + e.getMessage());
        }
    }

    @PostMapping("/admin/employees")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody create_employee request) {
        try {
            boolean result = employee_service.createEmployee(request);
            if (result) {
                return ResponseEntity.ok(
                        Collections.singletonMap("message", "Tạo tài khoản nhân viên thành công.")
                );
            }
            throw new RuntimeException("Không thể tạo nhân viên.");
        } catch (RuntimeException e) {
            throw new RuntimeException("Lỗi khi tạo tài khoản nhân viên: " + e.getMessage());
        }
    }

    @PutMapping("/employee/changeInfoEmployee")
    public ResponseEntity<?> changeInfoUser(@RequestBody update_employee request, HttpServletRequest httpServletRequest) {
        try {
            employee_service.updateEmployee(request);
            load_user_session.refreshUser(httpServletRequest.getSession());
            return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thông tin người dùng thành công"));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
