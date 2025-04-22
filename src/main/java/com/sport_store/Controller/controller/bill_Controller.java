package com.sport_store.Controller.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class bill_Controller {
    @GetMapping("/customer/bills")
    public String customerBills() {
        return "customer/bill";
    }
} 