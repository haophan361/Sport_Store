package com.appliance_store.Entity;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="image")
public class Image
{
    @Id
    private int image_id;
    private String image_url;
    @ManyToOne
    @JoinColumn(name="product_option_id")
    Product_Option product_option;
}
