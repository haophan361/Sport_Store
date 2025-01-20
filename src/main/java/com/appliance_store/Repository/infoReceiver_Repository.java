package com.appliance_store.Repository;

import com.appliance_store.Entity.Info_Receiver;
import com.appliance_store.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface infoReceiver_Repository extends JpaRepository<Info_Receiver, String>
{
    @Query("SELECT a FROM Info_Receiver a WHERE a.users =:user AND a.receiver_city =:city AND a.receiver_district =:district AND a.receiver_ward =:ward AND a.receiver_street =:street")
    public Info_Receiver existsAddress(Users user, String city, String district, String ward, String street);
}
