package com.sport_store.Controller.api;

import com.sport_store.DTO.request.updateUser_request;
import com.sport_store.Service.user_Service;
import com.sport_store.Util.LoadUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class userAPI {
    private final user_Service user_service;
    private final LoadUser load_user_session;

    @PostMapping("/user/changeInfoUser")
    public ResponseEntity<String> changeInfoUser(@RequestBody updateUser_request request, HttpServletRequest httpServletRequest) {
        user_service.updateUser(request);
        load_user_session.refreshUser(httpServletRequest.getSession());
        return ResponseEntity.ok("Cập nhật thông tin người dùng thành công");
    }
}
