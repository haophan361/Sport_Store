package com.sport_store.Controller.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class admin_user {
    @GetMapping("/admin/manageCustomer")
    public String manageCustomer() {
        return "admin/manageCustomer";
    }

    @GetMapping("/admin/manageEmployee")
    public String manageEmployee() {
        return "admin/manageEmployee";
    }
}
