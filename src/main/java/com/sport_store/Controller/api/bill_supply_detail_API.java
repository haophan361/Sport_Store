package com.sport_store.Controller.api;

import com.sport_store.DTO.response.bill_supply_detail_Response.bill_supply_detail_Response;
import com.sport_store.Entity.Bill_Supply_Details;
import com.sport_store.Service.bill_supply_detail_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class bill_supply_detail_API {
    private final bill_supply_detail_Service bill_supply_detail_service;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.###");

    @GetMapping("/admin/getBillSupplyDetail")
    public List<bill_supply_detail_Response> getBillSupplyDetail(@RequestParam String bill_supply_id) {
        List<bill_supply_detail_Response> responses = new ArrayList<>();
        List<Bill_Supply_Details> bill_supply_details = bill_supply_detail_service.getBillSupplyDetails(bill_supply_id);
        for (Bill_Supply_Details supply_detail : bill_supply_details) {
            BigDecimal total_price = supply_detail.getOption_cost().multiply(BigDecimal.valueOf(supply_detail.getOption_quantity()));
            bill_supply_detail_Response response = bill_supply_detail_Response
                    .builder()
                    .detail_id(supply_detail.getBill_supply_detail_id())
                    .product_name(supply_detail.getProduct_name())
                    .option_id(supply_detail.getProduct_options().getOption_id())
                    .size(supply_detail.getProduct_options().getOption_size())
                    .color(supply_detail.getProduct_options().getColors().getColor())
                    .quantity(supply_detail.getOption_quantity())
                    .cost(decimalFormat.format(supply_detail.getOption_cost()))
                    .total_price(decimalFormat.format(total_price))
                    .build();
            responses.add(response);
        }
        return responses;
    }
}
