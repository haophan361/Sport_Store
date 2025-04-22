package com.sport_store.Service;

import com.sport_store.Entity.*;
import com.sport_store.Repository.bill_details_Repository;
import com.sport_store.Repository.bills_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class bills_Service {
    private final bills_Repository bills_repository;
    private final bill_details_Repository bill_details_repository;
    private final info_receiver_Service info_receiver_service;
    private final cart_Service cart_service;
    private final product_option_Service product_option_service;

    @Transactional
    public void create_bill(Customers customer, String receiver_id, List<Carts> carts, boolean payment_method) {

        Receiver_Info receiver = info_receiver_service.get_receiver_by_id(receiver_id);
        Bills bill = Bills.builder()
                .bill_id(UUID.randomUUID().toString())
                .bill_purchase_date(LocalDateTime.now())
                .bill_status_payment(payment_method)
                .is_active(true)
                .receivers(receiver)
                .build();
        bill = bills_repository.save(bill);

        List<Bill_Details> bill_details = new ArrayList<>();
        BigDecimal total_cost = BigDecimal.ZERO;

        for (Carts cart : carts) {
            int quantity = cart.getCart_quantity();
            BigDecimal new_price = product_option_service.Get_newPrice(cart.getProduct_options().getDiscounts(), cart.getProduct_options().getOption_cost());
            Bill_Details detail = Bill_Details.builder()
                    .bill_detail_id(UUID.randomUUID().toString())
                    .product_name(cart.getProduct_options().getProducts().getProduct_name())
                    .product_cost(new_price)
                    .product_quantity(quantity)
                    .product_options(cart.getProduct_options())
                    .bills(bill)
                    .build();
            detail = bill_details_repository.save(detail);
            bill_details.add(detail);
            total_cost = total_cost.add(new_price.multiply(BigDecimal.valueOf(quantity)));
        }

        bill.setBill_total_cost(total_cost);
        bill.setBill_details(bill_details);
        bills_repository.save(bill);
        cart_service.deleteAllByCustomer(customer);
    }

    public Bills get_bill_by_id(String bill_id) {
        return bills_repository.findById(bill_id).orElse(null);
    }

    public List<Bills> get_bills_by_customer_id(String customer_id) {
        return bills_repository.findByReceivers_Customers_Customer_id(customer_id);
    }

    public void updateBill(Bills bill) {
        bills_repository.save(bill);
    }
} 