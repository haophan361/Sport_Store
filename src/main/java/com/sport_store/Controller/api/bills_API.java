package com.sport_store.Controller.api;

import com.sport_store.Entity.Bills;
import com.sport_store.Entity.Carts;
import com.sport_store.Service.bills_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class bills_API {
    private final bills_Service bills_service;

    @PostMapping("/create")
    public ResponseEntity<?> createBill(@RequestBody Map<String, Object> requestData) {
        try {
            String customerId = (String) requestData.get("customer_id");
            String receiverId = (String) requestData.get("receiver_id");
            String couponId = (String) requestData.get("coupon_id");
            List<Map<String, Object>> cartItemsData = (List<Map<String, Object>>) requestData.get("cart_items");
            
            if (customerId == null || receiverId == null || cartItemsData == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
            }
            
            // Convert cart items data to Carts objects
            List<Carts> cartItems = bills_service.convertToCartItems(customerId, cartItemsData);
            
            Bills bill = bills_service.create_bill(customerId, receiverId, cartItems, couponId);
            if (bill != null) {
                return ResponseEntity.ok(Map.of("bill_id", bill.getBill_id()));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to create bill"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "An error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/{bill_id}")
    public ResponseEntity<Bills> getBillById(@PathVariable String bill_id) {
        return ResponseEntity.ok(bills_service.get_bill_by_id(bill_id));
    }

    @GetMapping("/customer/{customer_id}")
    public ResponseEntity<List<Bills>> getBillsByCustomerId(@PathVariable String customer_id) {
        return ResponseEntity.ok(bills_service.get_bills_by_customer_id(customer_id));
    }

    @GetMapping("/orderconfirmation/{billId}")
    public String orderConfirmation(@PathVariable String billId, Model model) {
        Bills bill = bills_service.get_bill_by_id(billId);
        if (bill != null) {
            model.addAttribute("bill", bill);
            return "customer/orderconfirmation";
        }
        return "redirect:/customer/cart";
    }
} 