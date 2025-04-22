package com.sport_store.Service;

import com.sport_store.DTO.request.Info_receiver_Request.infoReceiver_request;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Receiver_Info;
import com.sport_store.Repository.infoReceiver_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class info_receiver_Service {
    private final infoReceiver_Repository infoReceiver_repository;
    private final customer_Service customer_service;

    public void add_infoReceiver(infoReceiver_request request) {
        Customers customer = customer_service.get_myInfo();
        Receiver_Info receiver = Receiver_Info.builder()
                .receiver_id(UUID.randomUUID().toString())
                .receiver_name(request.getName())
                .receiver_phone(request.getPhone())
                .receiver_city(request.getCity())
                .receiver_district(request.getDistrict())
                .receiver_ward(request.getWard())
                .receiver_street(request.getStreet())
                .customers(customer)
                .build();
        infoReceiver_repository.save(receiver);
    }

    public void update_infoReceiver(infoReceiver_request request) {
        Receiver_Info receiver = infoReceiver_repository.findByReceiverId(request.getReceiver_id());
        if (receiver != null) {
            receiver.setReceiver_name(request.getName());
            receiver.setReceiver_phone(request.getPhone());
            receiver.setReceiver_city(request.getCity());
            receiver.setReceiver_district(request.getDistrict());
            receiver.setReceiver_ward(request.getWard());
            receiver.setReceiver_street(request.getStreet());
            infoReceiver_repository.save(receiver);
        }
    }

    public void delete_infoReceiver(String receiver_id) {
        infoReceiver_repository.deleteById(receiver_id);
    }

    public List<Receiver_Info> get_all_infoReceiver(String customer_id) {
        Customers customer = customer_service.findCustomerByID(customer_id);
        return infoReceiver_repository.findByCustomers(customer);
    }

    public Receiver_Info get_receiver_by_id(String receiver_id) {
        return infoReceiver_repository.findByReceiverId(receiver_id);
    }
}
