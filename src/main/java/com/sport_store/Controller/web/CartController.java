package com.sport_store.Controller.web;

import com.sport_store.Util.LoadUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor

public class CartController {
    private final LoadUser login;

    @GetMapping("/customer/cart")
    public String shop(Model model, HttpSession session) {
        if (session.getAttribute("CustomerLogin") != null) {
            login.refreshUser(session);
            return "customer/cart";
        }
        return "redirect:/web/form_login";
    }
}
