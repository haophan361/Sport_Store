package com.sport_store.DTO.response.discount_Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class discount_Response {
    private int discount_id;
    private int discount_percentage;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime discount_start_date;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime discount_end_date;
}
