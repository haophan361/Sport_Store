package com.sport_store.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bill_supply_details")
public class Bill_Supply_Details {
    @Id
    private String bill_supply_detail_id;
    private String product_name;
    private BigDecimal option_cost;
    private int option_quantity;
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "option_id")
    private Product_Options product_options;
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bill_supply_id")
    private Bill_Supplies bill_supplies;
}
