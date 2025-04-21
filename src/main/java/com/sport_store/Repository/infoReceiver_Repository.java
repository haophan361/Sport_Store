package com.sport_store.Repository;

import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Receiver_Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface infoReceiver_Repository extends JpaRepository<Receiver_Info, String> {
    @Query("SELECT a FROM Receiver_Info a WHERE a.customers =:customer AND a.receiver_city =:city AND a.receiver_district =:district AND a.receiver_ward =:ward AND a.receiver_street =:street")
    public Receiver_Info existsAddress(Customers customer, String city, String district, String ward, String street);
    
    @Query("SELECT r FROM Receiver_Info r WHERE r.customers.customer_id = :customerId")
    List<Receiver_Info> findByCustomers_Customer_id(@Param("customerId") String customerId);

    List<Receiver_Info> findByCustomers(Customers customer);

    @Query("SELECT r FROM Receiver_Info r WHERE r.receiver_id = :receiverId")
    Receiver_Info findByReceiverId(@Param("receiverId") String receiverId);
}
