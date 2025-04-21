package com.sport_store.Controller.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class admin_dashboard {

    @GetMapping("/admin/dashboard")
    public String dashboardPage() {
        return "admin/dashboard";
    }
}
