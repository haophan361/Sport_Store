package com.sport_store.Repository;

import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Receiver_Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface infoReceiver_Repository extends JpaRepository<Receiver_Info, String> {
    @Query("SELECT a FROM Receiver_Info a WHERE a.customers =:customer AND a.receiver_city =:city AND a.receiver_district =:district AND a.receiver_ward =:ward AND a.receiver_street =:street")
    public Receiver_Info existsAddress(Customers customer, String city, String district, String ward, String street);
}
