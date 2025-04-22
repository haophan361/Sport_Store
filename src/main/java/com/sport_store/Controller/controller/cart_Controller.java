package com.sport_store.Controller.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class cart_Controller {

    @GetMapping("/customer/cart")
    public String shoppingCart() {
        return "customer/cart";
    }

    @GetMapping("/customer/checkout")
    public String checkout() {
        return "customer/checkout";
    }
}
