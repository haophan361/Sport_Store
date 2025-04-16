package com.sport_store.Service;

import com.sport_store.DTO.request.bill_supply_detail_Request.bill_supply_detail_Request;
import com.sport_store.Entity.Bill_Supplies;
import com.sport_store.Entity.Bill_Supply_Details;
import com.sport_store.Entity.Product_Options;
import com.sport_store.Repository.bill_supply_detail_Repository;
import com.sport_store.Repository.product_option_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class bill_supply_detail_Service {
    private final bill_supply_detail_Repository bill_supply_detail_repository;
    private final product_option_Service product_option_service;
    private final product_option_Repository product_option_repository;

    public Bill_Supply_Details saveSupplierBillDetail(bill_supply_detail_Request request, Bill_Supplies bill_supply) {
        Product_Options option = product_option_service.getProduct_Option(request.getOption_id());
        option.setOption_quantity(option.getOption_quantity() + request.getQuantity());
        product_option_repository.save(option);
        Bill_Supply_Details bill_supply_detail = Bill_Supply_Details
                .builder()
                .bill_supply_detail_id(UUID.randomUUID().toString())
                .product_name(request.getProduct_name())
                .product_options(option)
                .option_cost(request.getCost())
                .option_quantity(request.getQuantity())
                .bill_supplies(bill_supply)
                .build();
        return bill_supply_detail_repository.save(bill_supply_detail);
    }

    public BigDecimal totalBillSupply(List<Bill_Supply_Details> bill_supply_details) {
        BigDecimal total = BigDecimal.ZERO;
        for (Bill_Supply_Details bill_supply_detail : bill_supply_details) {
            total = total.add(bill_supply_detail.getOption_cost().multiply(BigDecimal.valueOf(bill_supply_detail.getOption_quantity())));
        }
        return total;
    }

    public List<Bill_Supply_Details> getBillSupplyDetails(String bill_supply_id) {
        return bill_supply_detail_repository.getBillSupplyDetails(bill_supply_id);
    }
}
