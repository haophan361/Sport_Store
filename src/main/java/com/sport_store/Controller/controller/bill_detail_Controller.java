package com.sport_store.Controller.controller;

import com.sport_store.Entity.Bills;
import com.sport_store.Service.bill_Service;
import com.sport_store.Service.category_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class bill_detail_Controller {
    private final bill_Service bill_service;
    private final category_Service category_service;

    @GetMapping("/customer/bill-detail/{billId}")
    public String billDetail(@PathVariable String billId, Model model) {
        Bills bill = bill_service.get_bill_by_id(billId);
        model.addAttribute("bill", bill);
        model.addAttribute("all_category", category_service.getAllCategories());
        return "/customer/bill_detail";
    }
}
