package com.sport_store.Controller.controller;

import com.sport_store.Entity.Carts;
import com.sport_store.Entity.Coupons;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Product_Options;
import com.sport_store.Service.CartService;
import com.sport_store.Service.CouponService;
import com.sport_store.Service.customer_Service;
import com.sport_store.Service.product_option_Service;
import com.sport_store.Util.LoadUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final LoadUser loadUser;
    private final customer_Service customerService;
    private final CartService cartService;
    private final product_option_Service productOptionService;
    private final CouponService couponService;

    public void LoadProduct(Model model, HttpSession session) {
        String customer_id = (String) session.getAttribute("customerId");
        Customers customers = customerService.finbyId(customer_id);
        List<Carts> cartsList = cartService.findAllCartsByCustomers(customers);
        double totalAmount = 0.0;
        int quantity = 0;

        if (cartsList != null && !cartsList.isEmpty()) {
            for (Carts cart : cartsList) {
                BigDecimal optionCost = cart.getProduct_options().getOption_cost();
                if (optionCost != null) {
                    totalAmount += optionCost.multiply(BigDecimal.valueOf(cart.getCart_quantity())).doubleValue();
                }
                quantity += cart.getCart_quantity();
            }
        }
        
        // Kiểm tra nếu có áp dụng coupon
        Coupons appliedCoupon = (Coupons) session.getAttribute("appliedCoupon");
        if (appliedCoupon != null) {
            // Tính toán giảm giá
            double discountAmount = (totalAmount * appliedCoupon.getCoupon_percentage()) / 100;
            double finalTotal = totalAmount - discountAmount;
            
            model.addAttribute("appliedCoupon", appliedCoupon);
            model.addAttribute("discountAmount", discountAmount);
            model.addAttribute("finalTotal", finalTotal);
        } else {
            model.addAttribute("finalTotal", totalAmount);
        }
        
        model.addAttribute("carts", cartsList);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("quantity", quantity);
    }

    @GetMapping("/customer/cart")
    public String shop(Model model, HttpSession session) {
        if (session.getAttribute("customerId") != null) {
            loadUser.refreshUser(session);
            LoadProduct(model, session);
            return "customer/cart";
        }
        return "redirect:/web/form_login";
    }

    @GetMapping("/customer/cart/apply-coupon")
    public String applyCoupon(Model model, HttpSession session, @RequestParam("couponId") String couponId) {
        if (session.getAttribute("customerId") != null) {
            // Xóa coupon cũ nếu có
            session.removeAttribute("appliedCoupon");
            
            // Kiểm tra coupon mới
            Coupons coupon = couponService.findCouponById(couponId);
            if (coupon != null && couponService.validateCoupon(couponId)) {
                session.setAttribute("appliedCoupon", coupon);
                // Giảm số lần sử dụng coupon
                couponService.decrementCouponAttempts(couponId);
            }
            
            return "redirect:/customer/cart";
        }
        return "redirect:/web/form_login";
    }

    @GetMapping("addtocart/{option_id}/{quantity}")
    public String addtocart(Model model, @PathVariable("option_id") int option_id,
                            @PathVariable("quantity") int quantity, HttpSession session) {
        String customer_id = (String) session.getAttribute("customerId");
        if (customer_id == null) {
            return "redirect:/web/form_login";
        }
        Customers customers = customerService.finbyId(customer_id);
        Product_Options product_options = productOptionService.getProduct_Option(option_id);
        Carts carts = cartService.findCart(customers, product_options);

        if (carts != null) {
            carts.setCart_quantity(carts.getCart_quantity() + quantity);
            cartService.updateCart(carts);
        } else {
            carts = new Carts();
            carts.setCart_id(UUID.randomUUID().toString());
            carts.setCustomers(customers);
            carts.setProduct_options(product_options);
            carts.setCart_quantity(quantity);
            cartService.addToCart(carts);
        }
        shop(model, session);
        return "customer/cart";
    }

    @GetMapping("subupdatequantitycart/{cartId}")
    public String subupdatequantitycart(Model model, @PathVariable("cartId") String cartId, HttpSession session) {
        Optional<Carts> carts = cartService.findCartById(cartId);
        if (carts.isPresent()) {
            Carts cart = carts.get();
            int currentQuantity = cart.getCart_quantity();
            if (currentQuantity > 1) {
                cart.setCart_quantity(currentQuantity - 1);
                cartService.updateCart(cart);
            }
            //else {
            //cartService.delete(cart);
            //}
        }
        shop(model, session);
        return "customer/cart";
    }

    @GetMapping("addupdatequantitycart/{cartId}")
    public String addupdatequantitycart(Model model, @PathVariable("cartId") String cartId, HttpSession session) {
        Optional<Carts> carts = cartService.findCartById(cartId);
        if (carts.isPresent()) {
            Carts cart = carts.get();
            cart.setCart_quantity(cart.getCart_quantity() + 1);
            cartService.updateCart(cart);
        }
        shop(model, session);
        return "customer/cart";
    }

    @GetMapping("/removecart/{cartId}")
    public String removeCart(@PathVariable("cartId") String cartId, HttpSession session) {
        Optional<Carts> cartOptional = cartService.findCartById(cartId);
        if (cartOptional.isPresent()) {
            cartService.delete(cartOptional.get());
        }
        return "redirect:/customer/cart";  // quay lại trang giỏ hàng sau khi xóa
    }
}
