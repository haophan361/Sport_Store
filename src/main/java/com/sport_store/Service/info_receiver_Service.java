package com.sport_store.Service;

import com.sport_store.DTO.request.Info_ReceiverDTO.change_infoReceiver_request;
import com.sport_store.Entity.Receiver_Info;
import com.sport_store.Repository.infoReceiver_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class info_receiver_Service {
    private final infoReceiver_Repository infoReceiver_repository;

    public void add_infoReceiver(Receiver_Info _receiverInfo) {
        infoReceiver_repository.save(_receiverInfo);
    }

    public void update_infoReceiver(change_infoReceiver_request request) {
        Receiver_Info _receiverInfo = infoReceiver_repository.getReferenceById(request.getReceiver_id());
        _receiverInfo.setReceiver_name(request.getName());
        _receiverInfo.setReceiver_phone(request.getPhone());
        _receiverInfo.setReceiver_city(request.getCity());
        _receiverInfo.setReceiver_district(request.getDistrict());
        _receiverInfo.setReceiver_ward(request.getWard());
        _receiverInfo.setReceiver_street(request.getStreet());
        infoReceiver_repository.save(_receiverInfo);
    }

    public void delete_infoReceiver(String receiver_id) {
        infoReceiver_repository.deleteById(receiver_id);
    }
}
