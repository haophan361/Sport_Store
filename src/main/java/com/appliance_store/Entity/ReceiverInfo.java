package com.appliance_store.Entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "receiver_info")
public class ReceiverInfo {
    @Id
    private String receiverId;
    private String receiverName;
    private String receiverPhone;
    private String receiverCity;
    private String receiverDistrict;
    private String receiverWard;
    private String receiverStreet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}