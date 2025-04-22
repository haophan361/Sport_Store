package com.sport_store.Controller.api;

import com.sport_store.DTO.request.cart_Request.addCart_request;
import com.sport_store.DTO.request.cart_Request.updateCart_request;
import com.sport_store.DTO.response.cart_Response.cart_Response;
import com.sport_store.Entity.Carts;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Product_Options;
import com.sport_store.Service.cart_Service;
import com.sport_store.Service.customer_Service;
import com.sport_store.Service.product_option_Service;
import com.sport_store.Util.LoadUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class cart_API {
    private final cart_Service cart_service;
    private final LoadUser loadUser;
    private final customer_Service customer_service;
    private final product_option_Service product_option_service;

    @GetMapping("/customer/getCart")
    public List<cart_Response> getCartByCustomer(HttpSession session) {
        String customer_id = (String) session.getAttribute("customerId");
        Customers customers = customer_service.findCustomerByID(customer_id);
        List<Carts> carts = cart_service.findAllCartsByCustomers(customers);
        List<cart_Response> responses = new ArrayList<>();
        for (Carts cart : carts) {
            BigDecimal new_price = product_option_service.Get_newPrice(cart.getProduct_options().getDiscounts(), cart.getProduct_options().getOption_cost());
            cart_Response response = cart_Response
                    .builder()
                    .cart_id(cart.getCart_id())
                    .image_url(cart.getProduct_options().getColors().getProduct_img().getFirst().getImages().getImage_url())
                    .product_name(cart.getProduct_options().getProducts().getProduct_name())
                    .color(cart.getProduct_options().getColors().getColor())
                    .size(cart.getProduct_options().getOption_size())
                    .cost(new_price)
                    .quantity(cart.getCart_quantity())
                    .total_price(new_price.multiply(BigDecimal.valueOf(cart.getCart_quantity())))
                    .build();
            responses.add(response);
        }
        return responses;
    }

    @PostMapping("/customer/addToCart")
    public ResponseEntity<?> addToCart(@RequestBody addCart_request request, HttpSession session) {
        String customer_id = (String) session.getAttribute("customerId");
        Customers customers = customer_service.findCustomerByID(customer_id);
        Product_Options product_options = product_option_service.getProduct_Option(request.getOption_id());
        Carts carts = cart_service.findCart(customers, product_options);

        if (carts != null) {
            carts.setCart_quantity(carts.getCart_quantity() + request.getQuantity());
            cart_service.updateCart(carts);
        } else {
            carts = new Carts();
            carts.setCart_id(UUID.randomUUID().toString());
            carts.setCustomers(customers);
            carts.setProduct_options(product_options);
            carts.setCart_quantity(request.getQuantity());
            cart_service.addToCart(carts);
        }
        loadUser.refreshCart(session);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm sản phẩm vào giỏ hàng thành công"));
    }

    @PutMapping("customer/updateQuantityCart")
    public void updateQuantityCart(@RequestBody updateCart_request request, HttpSession session) {
        Carts carts = cart_service.findCartById(request.getCart_id());
        carts.setCart_quantity(request.getQuantity());
        cart_service.updateCart(carts);
        loadUser.refreshCart(session);
    }

    @DeleteMapping("/customer/removeCart")
    public ResponseEntity<?> removeCart(@RequestParam String cart_id, HttpSession session) {
        cart_service.deleteCart(cart_id);
        loadUser.refreshCart(session);
        return ResponseEntity.ok(Collections.singletonMap("message", "Xóa sản phẩm khỏi giỏ hàng thành công"));
    }
}
