package com.sport_store.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bill_supplies")
public class Bill_Supplies {
    @Id
    private String bill_supply_id;
    private String supplier_name;
    private String supplier_address;
    private String supplier_phone;
    private LocalDateTime bill_supply_date;
    private BigDecimal bill_supply_cost;
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "bill_supplies")
    List<Bill_Supply_Details> bill_supply_details;
}
