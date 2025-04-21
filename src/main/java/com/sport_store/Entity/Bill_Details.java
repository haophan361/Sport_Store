package com.sport_store.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bill_details")
public class Bill_Details {
    @Id
    private String bill_detail_id;
    private String product_name;
    private BigDecimal product_cost;
    private int product_quantity;
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bills bills;
    @ManyToOne
    @JoinColumn(name = "option_id")
    private Product_Options product_options;
}
