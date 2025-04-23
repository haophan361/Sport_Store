package com.sport_store.Service;

import com.sport_store.Entity.*;
import com.sport_store.Repository.bill_Repository;
import com.sport_store.Repository.bill_detail_Repository;
import com.sport_store.Repository.product_option_Repository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class bill_Service {
    private final bill_Repository bill_repository;
    private final bill_detail_Repository bill_detail_repository;
    private final info_receiver_Service info_receiver_service;
    private final cart_Service cart_service;
    private final product_option_Service product_option_service;
    private final product_option_Repository product_option_repository;

    @Transactional
    public Bills create_bill(Customers customer, String receiver_id, List<Carts> carts, boolean payment_method) {

        Receiver_Info receiver = info_receiver_service.get_receiver_by_id(receiver_id);
        Bills bill = Bills.builder()
                .bill_id(UUID.randomUUID().toString())
                .bill_purchase_date(LocalDateTime.now())
                .bill_status_payment(payment_method)
                .is_active(true)
                .receivers(receiver)
                .build();
        bill = bill_repository.save(bill);

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
            detail = bill_detail_repository.save(detail);
            bill_details.add(detail);
            total_cost = total_cost.add(new_price.multiply(BigDecimal.valueOf(quantity)));
        }

        bill.setBill_total_cost(total_cost);
        bill.setBill_details(bill_details);
        cart_service.deleteAllByCustomer(customer);
        return bill_repository.save(bill);

    }

    public Bills get_bill_by_id(String bill_id) {
        return bill_repository.findById(bill_id).orElse(null);
    }

    public List<Bills> get_bills_by_customer_id(String customer_id) {
        return bill_repository.findByReceivers_Customers_Customer_id(customer_id);
    }

    public void updateBill(Bills bill) {
        bill_repository.save(bill);
    }

    public List<Bills> getAllBill(String status) {
        Specification<Bills> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            switch (status) {
                case "PENDING": {
                    predicates.add(root.get("bill_confirm_date").isNull());
                    predicates.add(criteriaBuilder.equal(root.get("is_active"), true));
                    break;
                }
                case "CONFIRMED": {
                    predicates.add(root.get("bill_confirm_date").isNotNull());
                    predicates.add(criteriaBuilder.equal(root.get("is_active"), true));
                    predicates.add(root.get("bill_receive_date").isNull());
                    break;
                }
                case "COMPLETED": {
                    predicates.add(root.get("bill_receive_date").isNotNull());
                    break;
                }
                case "CANCELLED_BY_CUSTOMER": {
                    predicates.add(root.get("employees").isNull());
                    predicates.add(criteriaBuilder.equal(root.get("is_active"), false));
                    break;
                }
                case "CANCELLED_BY_EMPLOYEE": {
                    predicates.add(root.get("employees").isNotNull());
                    predicates.add(criteriaBuilder.equal(root.get("is_active"), false));
                    break;
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        return bill_repository.findAll(specification);
    }

    @Transactional
    public void confirmBill(String bill_id, Employees employee) throws Exception {
        Bills bill = bill_repository.findById(bill_id).orElse(null);
        if (bill != null) {
            for (Bill_Details detail : bill.getBill_details()) {
                if (detail.getProduct_quantity() > detail.getProduct_options().getOption_quantity()) {
                    throw new Exception("Số lượng hàng của sản phẩm " +
                            detail.getProduct_options().getProducts().getProduct_name() + " " +
                            detail.getProduct_options().getColors().getColor() + " " +
                            detail.getProduct_options().getOption_size() + " không đủ để đáp ứng");
                }
                Product_Options option = detail.getProduct_options();
                option.setOption_quantity(option.getOption_quantity() - detail.getProduct_quantity());
                product_option_repository.save(option);
            }
            bill.setEmployees(employee);
            bill.setBill_confirm_date(LocalDateTime.now());
            bill.set_active(true);
            bill_repository.save(bill);
        }
    }
} 