package com.appliance_store.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_options")
public class ProductOption {
    @Id
    private String optionId;
    private String optionColor;
    private String optionSize;
    private int optionQuantity;
    private BigDecimal optionCost;
    private boolean isActive;
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}