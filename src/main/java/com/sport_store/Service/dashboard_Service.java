package com.sport_store.Service;

import com.sport_store.Entity.Bill_Details;
import com.sport_store.Entity.Bill_Supplies;
import com.sport_store.Entity.Bills;
import com.sport_store.Repository.bill_Repository;
import com.sport_store.Repository.bill_detail_Repository;
import com.sport_store.Repository.bill_supply_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class dashboard_Service {
    private final bill_Repository bill_repository;
    private final bill_supply_Repository bill_supplies_repository;
    private final bill_detail_Repository bill_detail_repository;

    public Map<String, BigDecimal> getMonthlyRevenue(LocalDateTime start, LocalDateTime end) {
        List<Bills> bills = bill_repository.findByBillPurchaseDateBetween(start, end);
        return bills.stream()
                .collect(Collectors.groupingBy(
                        bill -> bill.getBill_purchase_date().getYear() + "-" + bill.getBill_purchase_date().getMonthValue(),
                        Collectors.reducing(BigDecimal.ZERO, Bills::getBill_total_cost, BigDecimal::add)
                ));
    }

    // Thống kê chi phí theo tháng
    public Map<String, BigDecimal> getMonthlyCost(LocalDateTime start, LocalDateTime end) {
        List<Bill_Supplies> supplies = bill_supplies_repository.findByBillSupplyDateBetween(start, end);
        return supplies.stream()
                .collect(Collectors.groupingBy(
                        supply -> supply.getBill_supply_date().getYear() + "-" + supply.getBill_supply_date().getMonthValue(),
                        Collectors.reducing(BigDecimal.ZERO, Bill_Supplies::getBill_supply_cost, BigDecimal::add)
                ));
    }

    // Thống kê lượng mua sản phẩm
    public Map<String, Integer> getProductPurchaseQuantities(LocalDateTime start, LocalDateTime end) {
        List<Bill_Details> details = bill_detail_repository.findByBillsBillPurchaseDateBetween(start, end);
        return details.stream()
                .collect(Collectors.groupingBy(
                        Bill_Details::getProduct_name,
                        Collectors.summingInt(Bill_Details::getProduct_quantity)
                ));
    }

    public Map<String, BigDecimal> getAllMonthlyRevenue() {
        List<Bills> bills = bill_repository.findAllBillPurchase();
        return bills.stream()
                .collect(Collectors.groupingBy(
                        bill -> bill.getBill_purchase_date().getYear() + "-" + bill.getBill_purchase_date().getMonthValue(),
                        Collectors.reducing(BigDecimal.ZERO, Bills::getBill_total_cost, BigDecimal::add)
                ));
    }

    // Thống kê chi phí theo tháng
    public Map<String, BigDecimal> getAllMonthlyCost() {
        List<Bill_Supplies> supplies = bill_supplies_repository.findAllBillSupply();
        return supplies.stream()
                .collect(Collectors.groupingBy(
                        supply -> supply.getBill_supply_date().getYear() + "-" + supply.getBill_supply_date().getMonthValue(),
                        Collectors.reducing(BigDecimal.ZERO, Bill_Supplies::getBill_supply_cost, BigDecimal::add)
                ));
    }

    // Thống kê lượng mua sản phẩm
    public Map<String, Integer> getAllProductPurchaseQuantities() {
        List<Bill_Details> details = bill_detail_repository.findAllBillsBillPurchase();
        return details.stream()
                .collect(Collectors.groupingBy(
                        Bill_Details::getProduct_name,
                        Collectors.summingInt(Bill_Details::getProduct_quantity)
                ));
    }
}
