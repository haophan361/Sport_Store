package com.sport_store.Controller.api;

import com.sport_store.DTO.request.Info_ReceiverDTO.change_infoReceiver_request;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Receiver_Info;
import com.sport_store.Service.customer_Service;
import com.sport_store.Service.info_receiver_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class info_receiver_API {
    private final info_receiver_Service info_receiver_service;
    private final customer_Service customer_service;

    @PostMapping("/user/add_infoReceiver")
    public ResponseEntity<?> add_infoReceiver(@RequestBody change_infoReceiver_request request) {
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
        info_receiver_service.add_infoReceiver(receiver_info);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm thông tin người nhận thành công"));
    }

    @PostMapping("/user/update_infoReceiver")
    public ResponseEntity<?> update_infoReceiver(@RequestBody change_infoReceiver_request request) {
        info_receiver_service.update_infoReceiver(request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thông tin người nhận thành công"));
    }

    @PostMapping("/user/delete_infoReceiver")
    public ResponseEntity<?> delete_infoReceiver(@RequestBody String receiver_id) {
        info_receiver_service.delete_infoReceiver(receiver_id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Xóa thông tin người nhận thành công"));
    }
}
