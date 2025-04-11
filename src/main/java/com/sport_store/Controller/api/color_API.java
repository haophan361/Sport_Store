package com.sport_store.Controller.api;

import com.sport_store.Service.color_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class color_API {
    private final color_Service color_service;

}
