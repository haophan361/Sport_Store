package com.sport_store.Service;

import com.sport_store.Entity.Bill_Details;
import com.sport_store.Repository.bill_detail_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class bill_detail_Service {
    private final bill_detail_Repository bill_detail_repository;

    public List<Bill_Details> findByBillId(String bill_id) {
        return bill_detail_repository.findByBillId(bill_id);
    }
}
