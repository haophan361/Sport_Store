package com.sport_store.Controller.api;

import com.sport_store.DTO.request.bill_Request.bill_order_Request;
import com.sport_store.DTO.response.bill_Response.bill_Response;
import com.sport_store.DTO.response.cart_Response.cart_checkout_Response;
import com.sport_store.Entity.Bills;
import com.sport_store.Entity.Carts;
import com.sport_store.Entity.Customers;
import com.sport_store.Service.bill_Service;
import com.sport_store.Service.cart_Service;
import com.sport_store.Service.customer_Service;
import com.sport_store.Service.product_option_Service;
import com.sport_store.Util.LoadUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class bill_API {
    private final bill_Service bill_service;
    private final cart_Service cart_service;
    private final customer_Service customer_service;
    private final product_option_Service product_option_service;
    private final LoadUser loadUser;

    @PostMapping("/customer/order")
    public ResponseEntity<Map<String, String>> createBill(@RequestBody bill_order_Request request, HttpSession session) {
        Customers customers = customer_service.get_myInfo();
        List<Carts> carts = cart_service.findAllCartsByCustomers(customers);
        Bills bill = bill_service.create_bill(customers, request.getReceiver_id(), carts, request.isPayment_method());
        loadUser.refreshCart(session);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Tạo hóa đơn mua hành thành công");
        response.put("data", bill.getBill_id());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/getBill")
    public List<bill_Response> getBillsByCustomerId(HttpSession session) {
        String customer_id = (String) session.getAttribute("customerId");
        List<Bills> bills = bill_service.get_bills_by_customer_id(customer_id);
        List<bill_Response> bill_responses = new ArrayList<>();
        for (Bills bill : bills) {
            String employee_id = null;
            if (bill.getEmployees() != null) {
                employee_id = bill.getEmployees().getEmployee_id();
            }
            bill_Response bill_response = bill_Response
                    .builder()
                    .bill_id(bill.getBill_id())
                    .purchase_date(bill.getBill_purchase_date())
                    .confirmation_date(bill.getBill_confirm_date())
                    .receive_date(bill.getBill_receive_date())
                    .status_payment(bill.isBill_status_payment())
                    .total(bill.getBill_total_cost())
                    .active(bill.is_active())
                    .employee_id(employee_id)
                    .build();
            bill_responses.add(bill_response);
        }
        return bill_responses;
    }

    @PutMapping("/customer/cancel_order")
    public ResponseEntity<?> cancelOrder(@RequestParam String bill_id) {
        Bills bill = bill_service.get_bill_by_id(bill_id);
        bill.set_active(false);
        bill_service.updateBill(bill);
        return ResponseEntity.ok(Collections.singletonMap("message", "Hủy hóa đơn hàng thành công"));
    }

    @PutMapping("/customer/confirm_receive")
    public ResponseEntity<?> confirmReceive(@RequestParam String bill_id) {
        Bills bill = bill_service.get_bill_by_id(bill_id);
        bill.setBill_receive_date(LocalDateTime.now());
        bill.setBill_status_payment(true);
        bill_service.updateBill(bill);
        return ResponseEntity.ok(Collections.singletonMap("message", "Xác nhận thành công"));
    }

    @GetMapping("/customer/getCartCheckout")
    public List<cart_checkout_Response> getCartCheckout(HttpSession session) {
        String customer_id = (String) session.getAttribute("customerId");
        Customers customer = customer_service.findCustomerByID(customer_id);
        List<Carts> carts = cart_service.findAllCartsByCustomers(customer);
        List<cart_checkout_Response> responses = new ArrayList<>();
        for (Carts cart : carts) {
            BigDecimal new_price = product_option_service.Get_newPrice(cart.getProduct_options().getDiscounts(), cart.getProduct_options().getOption_cost());
            cart_checkout_Response response = cart_checkout_Response
                    .builder()
                    .cart_id(cart.getCart_id())
                    .product_name(cart.getProduct_options().getProducts().getProduct_name())
                    .color(cart.getProduct_options().getColors().getColor())
                    .size(cart.getProduct_options().getOption_size())
                    .quantity(cart.getCart_quantity())
                    .price(new_price)
                    .build();
            responses.add(response);
        }
        return responses;
    }
} 