package com.sport_store.Controller.controller;

import com.sport_store.Entity.Bills;
import com.sport_store.Entity.Carts;
import com.sport_store.Entity.Customers;
import com.sport_store.Service.CartService;
import com.sport_store.Service.bills_Service;
import com.sport_store.Service.customer_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final bills_Service billsService;
    private final customer_Service customerService;
    private final CartService cartService;

    @PostMapping("/api/orders/create")
    @ResponseBody
    public ResponseEntity<?> createOrder(@RequestBody Map<String, String> orderData) {
        try {
            String customerId = orderData.get("customer_id");
            String receiverId = orderData.get("receiver_id");
            
            if (customerId == null || receiverId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
            }
            
            Customers customer = customerService.finbyId(customerId);
            if (customer == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Customer not found"));
            }
            
            List<Carts> carts = cartService.findAllCartsByCustomers(customer);
            if (carts.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Cart is empty"));
            }

            Bills bill = billsService.create_bill(customerId, receiverId, carts);
            if (bill != null) {
                return ResponseEntity.ok(Map.of("bill_id", bill.getBill_id()));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to create order"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "An error occurred while processing your order"));
        }
    }

    @GetMapping("/orderconfirmation/{billId}")
    public String orderConfirmation(@PathVariable String billId, Model model) {
        Bills bill = billsService.get_bill_by_id(billId);
        if (bill != null) {
            model.addAttribute("bill", bill);
            return "customer/orderconfirmation";
        }
        return "redirect:/customer/cart";
    }

    @GetMapping("/customer/bills")
    public String customerBills(Model model, HttpSession session) {
        String customerId = (String) session.getAttribute("customerId");
        if (customerId == null) {
            return "redirect:/web/form_login";
        }
        
        List<Bills> bills = billsService.get_bills_by_customer_id(customerId);
        model.addAttribute("bills", bills);
        return "customer/bill-list";
    }

    @GetMapping("/customer/bill-detail/{billId}")
    public String billDetail(@PathVariable String billId, Model model, HttpSession session) {
        String customerId = (String) session.getAttribute("customerId");
        if (customerId == null) {
            return "redirect:/web/form_login";
        }
        
        Bills bill = billsService.get_bill_by_id(billId);
        
        // Check if bill exists and belongs to the current customer
        if (bill == null || !bill.getReceivers().getCustomers().getCustomer_id().equals(customerId)) {
            return "redirect:/customer/bills";
        }
        
        model.addAttribute("bill", bill);
        return "customer/order-detail";
    }

    @PostMapping("/customer/cancel-order")
    public String cancelOrder(@RequestParam("billId") String billId, HttpSession session, Model model) {
        String customerId = (String) session.getAttribute("customerId");
        if (customerId == null) {
            return "redirect:/web/form_login";
        }
        
        Bills bill = billsService.get_bill_by_id(billId);
        
        // Verify the bill belongs to this customer and can be canceled
        if (bill != null && bill.getReceivers().getCustomers().getCustomer_id().equals(customerId) 
            && bill.is_active() && bill.getEmployees() == null) {
            
            // Set inactive without setting employee
            bill.set_active(false);
            billsService.updateBill(bill);
            model.addAttribute("cancelSuccess", true);
        } else {
            model.addAttribute("cancelError", "Không thể hủy đơn hàng. Vui lòng kiểm tra lại.");
        }
        
        return "redirect:/customer/bills";
    }
} 