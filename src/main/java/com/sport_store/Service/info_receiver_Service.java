package com.sport_store.Service;

import com.sport_store.DTO.request.Info_receiver_Request.infoReceiver_request;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Receiver_Info;
import com.sport_store.Repository.infoReceiver_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class info_receiver_Service {
    private final infoReceiver_Repository infoReceiver_repository;
    private final customer_Service customer_service;

    public void add_infoReceiver(infoReceiver_request request) {
        Customers customer = customer_service.get_myInfo();
        Receiver_Info receiver_info = Receiver_Info
                .builder()
                .receiver_id(UUID.randomUUID().toString())
                .receiver_name(request.getName())
                .receiver_phone(request.getPhone())
                .receiver_city(request.getCity())
                .receiver_district(request.getDistrict())
                .receiver_ward(request.getWard())
                .receiver_street(request.getStreet())
                .customers(customer)
                .build();
        infoReceiver_repository.save(receiver_info);
    }

    public void update_infoReceiver(infoReceiver_request request) {
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
