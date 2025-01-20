package com.appliance_store.Entity;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="carts")
public class Carts 
{
    @Id
    private String cart_id;
    private int cart_quantity;
    @ManyToOne
    @JoinColumn(name="user_id")
    private Users users;
    @ManyToOne
    @JoinColumn(name="product_option_id")
    private Product_Option product_option;
}
