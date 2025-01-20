package com.appliance_store.Service;

import com.appliance_store.DTO.request.add_infoReceiver_request;
import com.appliance_store.Entity.Info_Receiver;
import com.appliance_store.Repository.infoReceiver_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class info_receiver_Service
{
    private final infoReceiver_Repository infoReceiver_repository;
    public void add_infoReceiver(Info_Receiver info_receiver)
    {
        infoReceiver_repository.save(info_receiver);
    }
    public void update_infoReceiver(add_infoReceiver_request request)
    {
        Info_Receiver info_receiver = infoReceiver_repository.getReferenceById(request.getReceiver_id());
        info_receiver.setReceiver_name(request.getName());
        info_receiver.setReceiver_phone(request.getPhone());
        info_receiver.setReceiver_city(request.getCity());
        info_receiver.setReceiver_district(request.getDistrict());
        info_receiver.setReceiver_ward(request.getWard());
        info_receiver.setReceiver_street(request.getStreet());
        infoReceiver_repository.save(info_receiver);
    }
    public void delete_infoReceiver(String receiver_id)
    {
        infoReceiver_repository.deleteById(receiver_id);
    }
}
