package com.sport_store.Controller.controller;

import com.sport_store.Service.category_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class cart_Controller {
    private final category_Service category_service;

    @GetMapping("/customer/cart")
    public String shoppingCart(Model model) {
        model.addAttribute("all_category", category_service.getAllCategories());
        return "customer/cart";
    }

    @GetMapping("/customer/checkout")
    public String checkout(Model model) {
        model.addAttribute("all_category", category_service.getAllCategories());
        return "customer/checkout";
    }
}
