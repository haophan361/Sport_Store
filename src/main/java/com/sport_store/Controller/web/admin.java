package com.sport_store.Controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class admin {

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "admin/dashboard"; // Trỏ đến templates/admin/dashboard.html
    }

    @GetMapping("/nhaphang")
    public String nhapHangPage() {
        return "admin/nhaphang"; // Trỏ đến templates/admin/nhaphang.html
    }

    @GetMapping("/sanpham")
    public String sanPhamPage() {
        return "admin/sanpham"; // Trỏ đến templates/admin/sanpham.html
    }
}
