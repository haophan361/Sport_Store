package com.appliance_store.Controller.api;

import com.appliance_store.DTO.request.add_infoReceiver_request;
import com.appliance_store.Entity.ReceiverInfo;
import com.appliance_store.Entity.User;
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
public class info_receiverAPI {
    private final info_receiver_Service info_receiver_service;
    private final user_Service user_service;
    @PostMapping("/user/add_infoReceiver")
    public ResponseEntity<String> add_infoReceiver(@RequestBody add_infoReceiver_request request) {
        User user = user_service.get_myInfo();
        ReceiverInfo _receiverInfo = ReceiverInfo
                .builder()
                .receiverId(UUID.randomUUID().toString())
                .receiverName(request.getName())
                .receiverPhone(request.getPhone())
                .receiverCity(request.getCity())
                .receiverDistrict(request.getDistrict())
                .receiverWard(request.getWard())
                .receiverStreet(request.getStreet())
                .user(user)
                .build();
        info_receiver_service.add_infoReceiver(_receiverInfo);
        return ResponseEntity.ok("Thêm thông tin người nhận thành công");
    }
    @PostMapping("/user/update_infoReceiver")
    public ResponseEntity<String> update_infoReceiver(@RequestBody add_infoReceiver_request request) {
        info_receiver_service.update_infoReceiver(request);
        return ResponseEntity.ok("Cập nhật thông tin người nhận thành công");
    }
    @PostMapping("/user/delete_infoReceiver")
    public ResponseEntity<String> delete_infoReceiver(@RequestBody String receiver_id) {
        info_receiver_service.delete_infoReceiver(receiver_id);
        return ResponseEntity.ok("Xóa thông tin người nhận thành công");
    }
}