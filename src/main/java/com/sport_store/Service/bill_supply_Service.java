package com.sport_store.Service;

import com.sport_store.DTO.request.bill_supply_Request.bill_supply_Request;
import com.sport_store.DTO.request.bill_supply_detail_Request.bill_supply_detail_Request;
import com.sport_store.Entity.Bill_Supplies;
import com.sport_store.Entity.Bill_Supply_Details;
import com.sport_store.Repository.bill_supply_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class bill_supply_Service {
    private final bill_supply_detail_Service bill_supply_detail_service;
    private final bill_supply_Repository bill_supply_repository;

    @Transactional
    public void saveBillSupply(bill_supply_Request request) {
        Bill_Supplies bill_supply = Bill_Supplies
                .builder()
                .bill_supply_id(UUID.randomUUID().toString())
                .supplier_name(request.getSupplier_name())
                .supplier_address(request.getSupplier_address())
                .supplier_phone(request.getSupplier_phone())
                .bill_supply_date(request.getBill_supply_date())
                .build();
        bill_supply_repository.save(bill_supply);

        List<Bill_Supply_Details> bill_supply_details = new ArrayList<>();
        for (bill_supply_detail_Request detail_request : request.getDetail_request()) {
            Bill_Supply_Details bill_supply_detail = bill_supply_detail_service.saveSupplierBillDetail(detail_request, bill_supply);
            bill_supply_details.add(bill_supply_detail);
        }
        BigDecimal total = bill_supply_detail_service.totalBillSupply(bill_supply_details);
        bill_supply.setBill_supply_cost(total);
        bill_supply.setBill_supply_details(bill_supply_details);
        bill_supply_repository.save(bill_supply);
    }

    public List<Bill_Supplies> getAllBillSupply() {
        return bill_supply_repository.findAll();
    }
}
