package com.sport_store.Service;

import com.sport_store.DTO.request.discount_Request.discount_request;
import com.sport_store.Entity.Discounts;
import com.sport_store.Repository.discount_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class discount_Service {
    private final discount_Repository discount_repository;

    public void saveDiscount(discount_request request) {
        Discounts discounts = Discounts
                .builder()
                .discount_percentage(request.getDiscount_percentage())
                .discount_start_date(request.getStart_date())
                .discount_end_date(request.getEnd_date())
                .is_active(request.isActive())
                .build();
        discount_repository.save(discounts);
    }

    public void deleteDiscount(int discount_id) {
        Discounts discount = discount_repository.findById(discount_id).orElse(null);
        if (discount != null) {
            discount.set_active(false);
            discount_repository.save(discount);
        }
    }

    public List<Discounts> getAllDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        return discount_repository.findAllActiveDiscount(now);
    }

    public Discounts getDiscount(int discount_id) {
        return discount_repository.findById(discount_id).orElse(null);
    }

    public boolean validateDiscount(Discounts discount) {
        LocalDateTime now = LocalDateTime.now();
        return discount.getDiscount_start_date().isBefore(now)
                || discount.getDiscount_end_date().isAfter(now)
                || !discount.is_active();
    }
}
