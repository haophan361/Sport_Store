package com.sport_store.Controller.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;



@Controller
@RequestMapping("/employee")
public class manage_order_Controller {

    @GetMapping("/manageOrder")
    public String manageOrder() {
        return "employee/manageOrder";
    }

    @GetMapping("/orderDetail/{billId}")
    public String orderDetail(@PathVariable String billId, Model model) {
        // Truyền đúng theo bảng bill_details
        model.addAttribute("billId", billId);
        model.addAttribute("bill_detail_id", "CT001");
        model.addAttribute("option_id", 1);
        model.addAttribute("product_name", "Áo thun thể thao");
        model.addAttribute("product_cost", 200000.00);
        model.addAttribute("product_quantity", 2);

        return "employee/orderDetail";
    }
}
