package com.sport_store.Controller.api;

import com.sport_store.DTO.request.Info_receiver_Request.infoReceiver_request;
import com.sport_store.DTO.response.info_receiver_Response.info_receiver_Response;
import com.sport_store.Entity.Receiver_Info;
import com.sport_store.Service.customer_Service;
import com.sport_store.Service.info_receiver_Service;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class info_receiver_API {
    private final info_receiver_Service info_receiver_service;
    private final customer_Service customer_service;

    @PostMapping("/customer/add_infoReceiver")
    public ResponseEntity<?> add_infoReceiver(@RequestBody infoReceiver_request request) {
        info_receiver_service.add_infoReceiver(request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm thông tin người nhận thành công"));
    }

    @PutMapping("/customer/update_infoReceiver")
    public ResponseEntity<?> update_infoReceiver(@RequestBody infoReceiver_request request) {
        info_receiver_service.update_infoReceiver(request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thông tin người nhận thành công"));
    }

    @DeleteMapping("/customer/delete_infoReceiver")
    public ResponseEntity<?> delete_infoReceiver(@RequestBody String receiver_id) {
        info_receiver_service.delete_infoReceiver(receiver_id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Xóa thông tin người nhận thành công"));
    }

    @GetMapping("/customer/getInfoReceiver")
    public List<info_receiver_Response> getInfoReceiver(HttpSession session) {
        String customer_id = (String) session.getAttribute("customerId");
        List<Receiver_Info> receiver_infos = info_receiver_service.get_all_infoReceiver(customer_id);
        List<info_receiver_Response> responses = new ArrayList<>();
        for (Receiver_Info receiver_info : receiver_infos) {
            info_receiver_Response response = info_receiver_Response
                    .builder()
                    .infoReceiver_id(receiver_info.getReceiver_id())
                    .name(receiver_info.getReceiver_name())
                    .phone(receiver_info.getReceiver_phone())
                    .address(receiver_info.getReceiver_city() + " " + receiver_info.getReceiver_district()
                            + " " + receiver_info.getReceiver_ward() + " " + receiver_info.getReceiver_street())
                    .build();
            responses.add(response);
        }
        return responses;
    }
}
