package com.appliance_store.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="discount")
public class Discount
{
    @Id
    private String discount_id;
    private int discount_percentage;
    private LocalDateTime discount_start_date;
    private LocalDateTime discount_end_date;
    private boolean is_active;
    @OneToMany(mappedBy = "discount")
    List<Products> products;
}
