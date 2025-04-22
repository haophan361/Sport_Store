package com.sport_store.Controller.api;

import com.sport_store.Service.dashboard_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class dashboard_API {

    private final dashboard_Service statistics_service;

    @GetMapping("/revenue")
    public Map<String, BigDecimal> getMonthlyRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return statistics_service.getMonthlyRevenue(start, end);
    }

    @GetMapping("/cost")
    public Map<String, BigDecimal> getMonthlyCost(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return statistics_service.getMonthlyCost(start, end);
    }

    @GetMapping("/product-quantities")
    public Map<String, Integer> getProductPurchaseQuantities(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return statistics_service.getProductPurchaseQuantities(start, end);
    }

    @GetMapping("/all-revenue")
    public Map<String, BigDecimal> getAllMonthlyRevenue() {
        return statistics_service.getAllMonthlyRevenue();
    }

    @GetMapping("/all-cost")
    public Map<String, BigDecimal> getAllMonthlyCost() {
        return statistics_service.getAllMonthlyCost();
    }

    @GetMapping("/all-product-quantities")
    public Map<String, Integer> getAllProductPurchaseQuantities() {
        return statistics_service.getAllProductPurchaseQuantities();
    }
}