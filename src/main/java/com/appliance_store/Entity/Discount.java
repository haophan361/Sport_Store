package com.appliance_store.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    private String discountId;
    private int discountPercentage;
    private LocalDateTime discountStartDate;
    private LocalDateTime discountEndDate;
    private boolean isActive;
}