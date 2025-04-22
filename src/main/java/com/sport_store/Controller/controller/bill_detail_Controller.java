package com.sport_store.Controller.controller;

import com.sport_store.Entity.Bills;
import com.sport_store.Service.bills_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class bill_detail_Controller {
    private final bills_Service bill_service;

    @GetMapping("/customer/bill-detail/{billId}")
    public String billDetail(@PathVariable String billId, Model model) {
        Bills bill = bill_service.get_bill_by_id(billId);
        model.addAttribute("bill", bill);
        return "/customer/bill_detail";
    }
}
