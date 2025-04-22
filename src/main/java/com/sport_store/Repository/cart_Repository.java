package com.sport_store.Repository;

import com.sport_store.Entity.Carts;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Product_Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface cart_Repository extends JpaRepository<Carts, String> {
    @Query("SELECT c FROM Carts c WHERE c.customers = :customers AND c.product_options = :productOptions")
    Carts findByCustomersAndProduct_options(@Param("customers") Customers customers,
                                            @Param("productOptions") Product_Options productOptions);

    List<Carts> findByCustomers(Customers customers);

    void deleteByCustomers(Customers customers);
}
