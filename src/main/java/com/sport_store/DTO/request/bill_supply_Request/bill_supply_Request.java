package com.sport_store.DTO.request.bill_supply_Request;

import com.sport_store.DTO.request.bill_supply_detail_Request.bill_supply_detail_Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class bill_supply_Request {
    private String supplier_name;
    private String supplier_phone;
    private String supplier_address;
    private LocalDateTime bill_supply_date;
    private BigDecimal bill_cost;
    List<bill_supply_detail_Request> detail_request;
}
