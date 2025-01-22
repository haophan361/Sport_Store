package com.appliance_store.Controller.api;

import com.appliance_store.Entity.User;
import com.appliance_store.Service.user_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class userAPI {
    private final user_Service user_service;
    @GetMapping("/getListUser")
    List<User> getListUser() {
        return user_service.getListUser();
    }
    @GetMapping("/getUser/{userID}")
    User getUsers(@PathVariable String userID) {
        return user_service.getUser(userID);
    }
    @GetMapping("/get_myInfo")
    User get_myInfo() {
        return user_service.get_myInfo();
    }
}
