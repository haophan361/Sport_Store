package com.sport_store.Controller.controller;

import com.sport_store.Service.category_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class category_Controller {
    private final category_Service category_service;

}
