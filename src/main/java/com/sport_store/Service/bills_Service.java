package com.sport_store.Service;

import com.sport_store.Entity.*;
import com.sport_store.Repository.bill_details_Repository;
import com.sport_store.Repository.bills_Repository;
import com.sport_store.Repository.CouponRepository;
import com.sport_store.Repository.product_option_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class bills_Service {
    private final bills_Repository bills_repository;
    private final bill_details_Repository bill_details_repository;
    private final customer_Service customer_service;
    private final info_receiver_Service info_receiver_service;
    private final CartService cart_service;
    private final product_option_Repository product_option_repository;
    private final CouponRepository couponRepository;

    @Transactional
    public Bills create_bill(String customer_id, String receiver_id, List<Carts> carts, String coupon_id) {
        Customers customer = customer_service.finbyId(customer_id);
        Receiver_Info receiver = info_receiver_service.get_receiver_by_id(receiver_id);
        
        if (customer == null || receiver == null) {
            return null;
        }

        Bills bill = Bills.builder()
                .bill_id(UUID.randomUUID().toString())
                .bill_purchase_date(LocalDateTime.now())
                .bill_status_payment(false)
                .is_active(true)
                .receivers(receiver)
                .build();

        // Nếu có coupon ID, tìm và gán vào bill
        if (coupon_id != null && !coupon_id.isEmpty()) {
            try {
                Coupons coupon = couponRepository.findById(coupon_id).orElse(null);
                if (coupon != null) {
                    bill.setCoupons(coupon);
                }
            } catch (Exception e) {
                // Log lỗi nếu cần
                System.out.println("Error finding coupon: " + e.getMessage());
            }
        }

        // Save the bill first to ensure it has an ID
        bill = bills_repository.save(bill);

        List<Bill_Details> bill_details = new ArrayList<>();
        BigDecimal total_cost = BigDecimal.ZERO;

        for (Carts cart : carts) {
            Product_Options option = cart.getProduct_options();
            BigDecimal cost = option.getOption_cost();
            int quantity = cart.getCart_quantity();

            Bill_Details detail = Bill_Details.builder()
                    .bill_detail_id(UUID.randomUUID().toString())
                    .product_name(option.getProducts().getProduct_name())
                    .product_cost(cost)
                    .product_quantity(quantity)
                    .product_options(option)
                    .bills(bill)
                    .build();

            // Save each detail individually to ensure proper relationship
            detail = bill_details_repository.save(detail);
            bill_details.add(detail);
            total_cost = total_cost.add(cost.multiply(BigDecimal.valueOf(quantity)));
        }

        // Tính toán giảm giá nếu có coupon
        if (bill.getCoupons() != null) {
            int discount_percentage = bill.getCoupons().getCoupon_percentage();
            BigDecimal discount_amount = total_cost.multiply(BigDecimal.valueOf(discount_percentage)).divide(BigDecimal.valueOf(100));
            total_cost = total_cost.subtract(discount_amount);
        }

        // Update bill with total cost and details
        bill.setBill_total_cost(total_cost);
        bill.setBill_details(bill_details);
        bill = bills_repository.save(bill);

        // Delete all cart items for the customer
        cart_service.deleteAllByCustomer(customer);

        return bill;
    }

    @Transactional
    public Bills create_bill(String customer_id, String receiver_id, List<Carts> carts) {
        return create_bill(customer_id, receiver_id, carts, null);
    }

    public Bills get_bill_by_id(String bill_id) {
        return bills_repository.findById(bill_id).orElse(null);
    }

    public List<Bills> get_bills_by_customer_id(String customer_id) {
        return bills_repository.findByReceivers_Customers_Customer_id(customer_id);
    }
    
    /**
     * Convert cart items data from frontend to Carts objects
     * @param customerId The customer ID
     * @param cartItemsData List of cart items data from frontend
     * @return List of Carts objects
     */
    public List<Carts> convertToCartItems(String customerId, List<Map<String, Object>> cartItemsData) {
        List<Carts> cartItems = new ArrayList<>();
        Customers customer = customer_service.finbyId(customerId);
        
        if (customer == null) {
            return cartItems;
        }
        
        for (Map<String, Object> itemData : cartItemsData) {
            Object productOptionIdObj = itemData.get("product_option_id");
            Object quantityObj = itemData.get("quantity");
            
            if (productOptionIdObj != null && quantityObj != null) {
                int productOptionId;
                int quantity;
                
                // Handle different types for product_option_id
                if (productOptionIdObj instanceof String) {
                    productOptionId = Integer.parseInt((String) productOptionIdObj);
                } else if (productOptionIdObj instanceof Integer) {
                    productOptionId = (Integer) productOptionIdObj;
                } else {
                    continue; // Skip this item if product_option_id is not a valid type
                }
                
                // Handle different types for quantity
                if (quantityObj instanceof String) {
                    quantity = Integer.parseInt((String) quantityObj);
                } else if (quantityObj instanceof Integer) {
                    quantity = (Integer) quantityObj;
                } else {
                    continue; // Skip this item if quantity is not a valid type
                }
                
                Product_Options productOption = product_option_repository.findById(productOptionId).orElse(null);
                
                if (productOption != null) {
                    Carts cart = Carts.builder()
                            .cart_id(UUID.randomUUID().toString())
                            .customers(customer)
                            .product_options(productOption)
                            .cart_quantity(quantity)
                            .build();
                    
                    cartItems.add(cart);
                }
            }
        }
        
        return cartItems;
    }
} 