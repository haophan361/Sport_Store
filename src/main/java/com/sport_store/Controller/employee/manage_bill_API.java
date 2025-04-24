package com.sport_store.Controller.employee;

import com.sport_store.DTO.response.bill_Response.bill_manage_Response;
import com.sport_store.DTO.response.bill_detail_Response.manage_bill_detail_Response;
import com.sport_store.Entity.Bill_Details;
import com.sport_store.Entity.Bills;
import com.sport_store.Entity.Employees;
import com.sport_store.Service.account_Service;
import com.sport_store.Service.bill_Service;
import com.sport_store.Service.bill_detail_Service;
import com.sport_store.Service.employee_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class manage_bill_API {
    private final bill_Service bill_service;
    private final bill_detail_Service bill_detail_service;
    private final account_Service account_service;
    private final employee_Service employee_service;

    @GetMapping("/employee/getAllBill")
    public List<bill_manage_Response> getAllBill(@RequestParam(value = "type_status", required = false, defaultValue = "ALL") String type_status) {
        List<Bills> bills = bill_service.getAllBill(type_status);
        List<bill_manage_Response> responses = new ArrayList<>();
        for (Bills bill : bills) {
            String employee_email = null;
            if (bill.getEmployees() != null) {
                employee_email = bill.getEmployees().getEmployee_email();
            }
            bill_manage_Response response = bill_manage_Response
                    .builder()
                    .bill_id(bill.getBill_id())
                    .total_amount(bill.getBill_total_cost())
                    .purchase_date(bill.getBill_purchase_date())
                    .confirmation_date(bill.getBill_confirm_date())
                    .receive_date(bill.getBill_receive_date())
                    .customer_email(bill.getReceivers().getCustomers().getCustomer_email())
                    .employee_email(employee_email)
                    .active(bill.is_active())
                    .paid(bill.isBill_status_payment())
                    .build();
            responses.add(response);
        }
        return responses;
    }

    @GetMapping("/employee/getBillDetail")
    public List<manage_bill_detail_Response> getBillDetail(@RequestParam String bill_id) {
        List<Bill_Details> bill_details = bill_detail_service.findByBillId(bill_id);
        List<manage_bill_detail_Response> responses = new ArrayList<>();
        for (Bill_Details bill_detail : bill_details) {
            manage_bill_detail_Response response = manage_bill_detail_Response
                    .builder()
                    .bill_id(bill_detail.getBills().getBill_id())
                    .bill_detail_id(bill_detail.getBill_detail_id())
                    .product_name(bill_detail.getProduct_name())
                    .option_id(bill_detail.getProduct_options().getOption_id())
                    .color(bill_detail.getProduct_options().getColors().getColor())
                    .size(bill_detail.getProduct_options().getOption_size())
                    .stock(bill_detail.getProduct_options().getOption_quantity())
                    .price(bill_detail.getProduct_cost())
                    .quantity(bill_detail.getProduct_quantity())
                    .total_price(bill_detail.getProduct_cost().multiply(BigDecimal.valueOf(bill_detail.getProduct_quantity())))
                    .bill_total_price(bill_detail.getBills().getBill_total_cost())
                    .build();
            responses.add(response);
        }
        return responses;
    }

    @PutMapping("/employee/confirm_bill")
    public ResponseEntity<?> confirmBill(@RequestParam String bill_id) throws Exception {
        Employees employee = employee_service.getEmployee(account_service.getEmail());
        try {
            bill_service.confirmBill(bill_id, employee);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Xác nhận đơn hàng thành công"));
    }

    @PutMapping("/employee/cancel_bill")
    public ResponseEntity<?> cancelBill(@RequestParam String bill_id) {
        String email = account_service.getEmail();
        Bills bill = bill_service.get_bill_by_id(bill_id);
        Employees employee = employee_service.getEmployee(email);
        bill.setEmployees(employee);
        bill.setBill_confirm_date(LocalDateTime.now());
        bill.set_active(false);
        bill_service.updateBill(bill);
        return ResponseEntity.ok(Collections.singletonMap("message", "Hủy đơn hàng thành công"));
    }
}
