package com.sport_store.Controller.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class bill_supply_Controller {
    @GetMapping("/admin/manageSupplierBill")
    public String manageSupplierBill() {
        return "/admin/supplier_bill";
    }
}
