package com.sport_store.Controller.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class admin_product_Controller {
    @GetMapping("/admin/manageProduct")
    public String manageProduct() {
        return "admin/product";
    }
}
