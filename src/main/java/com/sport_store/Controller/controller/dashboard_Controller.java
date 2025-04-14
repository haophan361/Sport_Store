package com.sport_store.Controller.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class dashboard_Controller {

    @GetMapping("/admin/dashboard")
    public String dashboardPage() {
        return "admin/dashboard";
    }
}
