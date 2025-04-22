package com.sport_store.Service;

import com.sport_store.Entity.Carts;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Product_Options;
import com.sport_store.Repository.cart_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class cart_Service {
    private final cart_Repository cart_repository;

    public Carts findCart(Customers customers, Product_Options options) {
        return cart_repository.findByCustomersAndProduct_options(customers, options);
    }

    public void updateCart(Carts cart) {
        cart_repository.save(cart);
    }

    public void addToCart(Carts carts) {
        cart_repository.save(carts);
    }

    public List<Carts> findAllCartsByCustomers(Customers customers) {
        return cart_repository.findByCustomers(customers);
    }

    public Carts findCartById(String cart_id) {
        return cart_repository.findById(cart_id).orElse(null);
    }

    public void deleteCart(String cart_id) {
        cart_repository.deleteById(cart_id);
    }

    @Transactional
    public void deleteAllByCustomer(Customers customers) {
        cart_repository.deleteByCustomers(customers);
    }
}
