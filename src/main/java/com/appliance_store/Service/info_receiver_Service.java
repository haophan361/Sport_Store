package com.appliance_store.Service;

import com.appliance_store.DTO.request.add_infoReceiver_request;
import com.appliance_store.Entity.ReceiverInfo;
import com.appliance_store.Repository.infoReceiver_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class info_receiver_Service {
    private final infoReceiver_Repository infoReceiver_repository;

    public void add_infoReceiver(ReceiverInfo _receiverInfo) {
        infoReceiver_repository.save(_receiverInfo);
    }

    public void update_infoReceiver(add_infoReceiver_request request) {
        ReceiverInfo _receiverInfo = infoReceiver_repository.getReferenceById(request.getReceiver_id());
        _receiverInfo.setReceiverName(request.getName());
        _receiverInfo.setReceiverPhone(request.getPhone());
        _receiverInfo.setReceiverCity(request.getCity());
        _receiverInfo.setReceiverDistrict(request.getDistrict());
        _receiverInfo.setReceiverWard(request.getWard());
        _receiverInfo.setReceiverStreet(request.getStreet());
        infoReceiver_repository.save(_receiverInfo);
    }

    public void delete_infoReceiver(String receiver_id) {
        infoReceiver_repository.deleteById(receiver_id);
    }
}
