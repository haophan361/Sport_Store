package com.appliance_store.Controller.api;

import com.appliance_store.DTO.request.add_infoReceiver_request;
import com.appliance_store.Entity.Info_Receiver;
import com.appliance_store.Entity.Users;
import com.appliance_store.Service.info_receiver_Service;
import com.appliance_store.Service.user_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class info_receiverAPI
{
    private final info_receiver_Service info_receiver_service;
    private final user_Service user_service;
    @PostMapping("/user/add_infoReceiver")
    public ResponseEntity<String> add_infoReceiver(@RequestBody add_infoReceiver_request request)
    {
        Users user=user_service.get_myInfo();
        Info_Receiver info_receiver = Info_Receiver
                .builder()
                .receiver_id(UUID.randomUUID().toString())
                .receiver_name(request.getName())
                .receiver_phone(request.getPhone())
                .receiver_city(request.getCity())
                .receiver_district(request.getDistrict())
                .receiver_ward(request.getWard())
                .receiver_street(request.getStreet())
                .users(user)
                .build();
        info_receiver_service.add_infoReceiver(info_receiver);
        return ResponseEntity.ok("Thêm thông tin người nhận thành công");
    }
    @PostMapping("/user/update_infoReceiver")
    public ResponseEntity<String> update_infoReceiver(@RequestBody add_infoReceiver_request request)
    {
        info_receiver_service.update_infoReceiver(request);
        return ResponseEntity.ok("Cập nhật thông tin người nhận thành công");
    }
    @PostMapping("/user/delete_infoReceiver")
    public ResponseEntity<String> delete_infoReceiver(@RequestBody String receiver_id)
    {
        info_receiver_service.delete_infoReceiver(receiver_id);
        return ResponseEntity.ok("Xóa thông tin người nhận thành công");
    }
}
