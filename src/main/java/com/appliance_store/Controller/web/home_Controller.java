package com.appliance_store.Controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class home_Controller {
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/customer/cart")
    public String cart() {
        return "cart";
    }
}