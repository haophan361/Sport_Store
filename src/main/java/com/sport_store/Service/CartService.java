package com.sport_store.Service;

import com.sport_store.Entity.Carts;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Product_Options;
import com.sport_store.Repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Carts findCart(Customers customers, Product_Options options) {
        return cartRepository.findByCustomersAndProduct_options(customers, options);
    }

    public void updateCart(Carts cart) {
        cartRepository.save(cart);
    }

    public Carts addToCart(Carts carts) {
        return cartRepository.save(carts);
    }

    public List<Carts> findAllCartsByCustomers(Customers customers) {
        return cartRepository.findByCustomers(customers);
    }

    public Optional<Carts> findCartById(String cartId) {
        return cartRepository.findById(cartId);
    }

    public void delete(Carts carts) {
        cartRepository.delete(carts);
    }
    
    @Transactional
    public void deleteAllByCustomer(Customers customers) {
        cartRepository.deleteByCustomers(customers);
    }
}
