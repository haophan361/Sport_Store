package com.sport_store.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "receiver_info")
public class Receiver_Info {
    @Id
    private String receiver_id;
    private String receiver_name;
    private String receiver_phone;
    private String receiver_city;
    private String receiver_district;
    private String receiver_ward;
    private String receiver_street;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private Users users;
    @OneToMany(mappedBy = "receiver")
    private List<Bills> bill;
}
