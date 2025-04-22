package com.sport_store.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")

public class Customers {
    @Id
    private String customer_id;
    private String customer_email;
    private String customer_name;
    private LocalDate customer_date_of_birth;
    private String customer_phone;
    @ToString.Exclude
    @OneToMany(mappedBy = "customers")
    private List<Receiver_Info> Receiver_Info;
    @ToString.Exclude
    @OneToMany(mappedBy = "customers")
    private List<Carts> carts;
}
