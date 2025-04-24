package com.sport_store.Controller.employee;

import com.sport_store.Entity.Bills;
import com.sport_store.Service.bill_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class manage_bill_Controller {
    private final bill_Service bill_service;

    @GetMapping("/employee/manageOrder")
    public String manageOrder() {
        return "employee/manageOrder";
    }

    @GetMapping("/employee/orderDetail")
    public String orderDetail(Model model, @RequestParam String bill_id) {
        Bills bill = bill_service.get_bill_by_id(bill_id);
        model.addAttribute("bill", bill);
        return "employee/orderDetail";
    }
}
