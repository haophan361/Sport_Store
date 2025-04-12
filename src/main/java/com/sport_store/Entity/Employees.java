package com.sport_store.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employees {
    @Id
    private String employee_id;
    private String employee_name;
    private String employee_address;
    private String employee_phone;
    private String employee_email;
    private LocalDate employee_date_of_birth;
    private boolean employee_gender;
    @OneToMany(mappedBy = "employees")
    private List<Bills> bills;
}
