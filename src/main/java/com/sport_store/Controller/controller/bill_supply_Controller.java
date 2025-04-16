package com.sport_store.Controller.controller;

import com.sport_store.Service.product_option_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class bill_supply_Controller {
    private final product_option_Service product_option_service;

    @GetMapping("/admin/manageSupplierBill")
    public String manageSupplierBill() {
        return "/admin/supplier_bill";
    }
}
