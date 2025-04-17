package com.sport_store.Controller.controller;

import com.sport_store.Entity.Carts;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Receiver_Info;
import com.sport_store.Service.CartService;
import com.sport_store.Service.customer_Service;
import com.sport_store.Service.info_receiver_Service;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class checkout_Controller {
    private final CartService cartService;
    private final customer_Service customerService;
    private final info_receiver_Service info_receiver_service;

    @GetMapping("customer/checkout")
    public String checkout(Model model, HttpSession session) {
        String customer_id = (String) session.getAttribute("customerId");
        if (customer_id == null) {
            return "redirect:/web/form_login";
        }
        Customers customer = customerService.finbyId(customer_id);
        List<Receiver_Info> receivers = info_receiver_service.get_all_infoReceiver(customer_id);

        List<Carts> cartsList = cartService.findAllCartsByCustomers(customer);
        double totalAmount = 0.0;
        int quantity = 0;

        if (cartsList != null && !cartsList.isEmpty()) {
            for (Carts cart : cartsList) {
                BigDecimal optionCost = cart.getProduct_options().getOption_cost();
                if (optionCost != null) {
                    totalAmount += optionCost.multiply(BigDecimal.valueOf(cart.getCart_quantity())).doubleValue();
                }
                quantity += cart.getCart_quantity();
            }
        }
        
        // Thêm dữ liệu vào model
        model.addAttribute("carts", cartsList);
        model.addAttribute("customer", customer);
        model.addAttribute("receivers", receivers);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("customerId", customer_id);
        
        return "customer/checkout";
    }
} 