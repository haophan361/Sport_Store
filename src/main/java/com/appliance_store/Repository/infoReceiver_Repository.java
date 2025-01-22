package com.appliance_store.Repository;

import com.appliance_store.Entity.ReceiverInfo;
import com.appliance_store.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface infoReceiver_Repository extends JpaRepository<ReceiverInfo, String> {
    @Query("SELECT a FROM ReceiverInfo a WHERE a.user =:user AND a.receiverCity =:city AND a.receiverDistrict =:district AND a.receiverWard =:ward AND a.receiverStreet =:street")
    public ReceiverInfo existsAddress(User user, String city, String district, String ward, String street);
}
