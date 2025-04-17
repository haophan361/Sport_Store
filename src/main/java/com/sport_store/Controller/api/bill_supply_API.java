package com.sport_store.Controller.api;

import com.sport_store.DTO.request.bill_supply_Request.bill_supply_Request;
import com.sport_store.DTO.response.bill_supply_Response.bill_supply_Response;
import com.sport_store.DTO.response.product_option_Response.product_option_bill_supply_Response;
import com.sport_store.Entity.Bill_Supplies;
import com.sport_store.Entity.Product_Options;
import com.sport_store.Service.bill_supply_Service;
import com.sport_store.Service.product_option_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class bill_supply_API {
    private final bill_supply_Service bill_supply_service;
    private final product_option_Service product_option_service;

    @PostMapping("/admin/insert_supplier_bill")
    public ResponseEntity<?> insert_supplier_bill(@RequestBody bill_supply_Request request) {
        bill_supply_service.saveBillSupply(request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm hóa đơn nhập hàng thành công"));
    }

    @GetMapping("/getAllBillSupply")
    public List<bill_supply_Response> getAllBillSupply() {
        List<Bill_Supplies> bill_supplies = bill_supply_service.getAllBillSupply();
        List<bill_supply_Response> responses = new ArrayList<>();
        for (Bill_Supplies bill_supply : bill_supplies) {
            bill_supply_Response response = bill_supply_Response
                    .builder()
                    .bill_supply_id(bill_supply.getBill_supply_id())
                    .supplier_name(bill_supply.getSupplier_name())
                    .supplier_phone(bill_supply.getSupplier_phone())
                    .supplier_address(bill_supply.getSupplier_address())
                    .bill_supply_cost(bill_supply.getBill_supply_cost())
                    .bill_supply_date(bill_supply.getBill_supply_date())
                    .build();
            responses.add(response);
        }
        return responses;
    }

    @GetMapping("/getOption_BillSupply")
    public List<product_option_bill_supply_Response> getOption_BillSupply() {
        List<Product_Options> options = product_option_service.getAllProduct_Option();
        List<product_option_bill_supply_Response> responses = new ArrayList<>();
        for (Product_Options option : options) {
            product_option_bill_supply_Response response = product_option_bill_supply_Response
                    .builder()
                    .product_option_id(option.getOption_id())
                    .product_name(option.getProducts().getProduct_name())
                    .color(option.getColors().getColor())
                    .size(option.getOption_size())
                    .build();
            responses.add(response);
        }
        return responses;
    }
}
