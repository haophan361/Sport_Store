package com.sport_store.Controller.controller;

import com.sport_store.DTO.response.customer_response.customer_changeInfo_Response;
import com.sport_store.DTO.response.employee_response.employee_changeInfo_Response;
import com.sport_store.DTO.response.info_receiver_Response.info_receiver_changeInfo_Response;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Employees;
import com.sport_store.Entity.Receiver_Info;
import com.sport_store.Service.customer_Service;
import com.sport_store.Service.employee_Service;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class customer_Controller {
    private final customer_Service customer_service;
    private final employee_Service employee_service;


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
        return "user/changePassword";
    }

    @GetMapping("/form/forgetPassword")
    public String getForm_ForgetPassword() {
        return "user/forgetPassword";
    }

    @GetMapping("/form/check_codeVerifyEmail")
    public String getForm_checkCodeResetPassword() {
        return "user/verify_Email";
    }

    @GetMapping("/form/request_forgetPassword")
    public String getForm_mail_forgetPassword() {
        return "user/request_forgetPassword";
    }

    @GetMapping("/web/admin")
    public String showWebAdminPage() {
        return "web/admin";
    }

    @GetMapping("/form/changeInfoCustomer")
    public String getForm_UpdateInfoUser(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (session.getAttribute("role") == "CUSTOMER") {
            Customers customers = customer_service.getUserByEmail(email);
            List<info_receiver_changeInfo_Response> receiver_responses = new ArrayList<>();
            for (Receiver_Info info : customers.getReceiver_Info()) {
                info_receiver_changeInfo_Response response = info_receiver_changeInfo_Response
                        .builder()
                        .receiver_id(info.getReceiver_id())
                        .receiver_name(info.getReceiver_name())
                        .receiver_phone(info.getReceiver_phone())
                        .receiver_city(info.getReceiver_city())
                        .receiver_district(info.getReceiver_district())
                        .receiver_ward(info.getReceiver_ward())
                        .receiver_street(info.getReceiver_street())
                        .build();
                receiver_responses.add(response);
            }
            customer_changeInfo_Response customer_response = customer_changeInfo_Response
                    .builder()
                    .customer_id(customers.getCustomer_id())
                    .customer_name(customers.getCustomer_name())
                    .customer_email(customers.getCustomer_email())
                    .customer_phone(customers.getCustomer_phone())
                    .customer_date_of_birth(customers.getCustomer_date_of_birth())
                    .receiver_Info(receiver_responses)
                    .build();
            model.addAttribute("customer", customer_response);
        } else {
            Employees employee = employee_service.getEmployee(email);
            employee_changeInfo_Response employee_response = employee_changeInfo_Response
                    .builder()
                    .employee_id(employee.getEmployee_id())
                    .employee_name(employee.getEmployee_name())
                    .employee_date_of_birth(employee.getEmployee_date_of_birth())
                    .employee_email(employee.getEmployee_email())
                    .employee_phone(employee.getEmployee_phone())
                    .employee_gender(employee.isEmployee_gender())
                    .employee_address(employee.getEmployee_address())
                    .build();
            model.addAttribute("employee", employee_response);
        }
        return "user/changeInfoUser";
    }

}
