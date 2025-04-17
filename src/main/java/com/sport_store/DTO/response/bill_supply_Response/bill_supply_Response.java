package com.sport_store.DTO.response.bill_supply_Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class bill_supply_Response {
    private String bill_supply_id;
    private String supplier_name;
    private String supplier_address;
    private String supplier_phone;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime bill_supply_date;
    private BigDecimal bill_supply_cost;
}
