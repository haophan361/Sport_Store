package com.appliance_store.Controller.web;

import com.appliance_store.Entity.User;
import com.appliance_store.Service.user_Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class user_Controller {
    private final user_Service user_service;
    @GetMapping("/web/form_register")
    public String getForm_Register() {
        return "web/register";
    }
    @GetMapping("/web/form_login")
    public String getForm_Login() {
        return "web/login";
    }
    @GetMapping("/form/changePassword")
    public String getForm_changePassword() {
        return "web/changePassword";
    }
    @GetMapping("/form/changeInfoUser")
    public String getForm_UpdateInfoUser(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session==null) {
            return "web/login";
        }
        String email= (String) session.getAttribute("email");
        User user = user_service.getUserByEmail(email);
        model.addAttribute("user",user);
        return "web/changeInfoUser";
    }
}
